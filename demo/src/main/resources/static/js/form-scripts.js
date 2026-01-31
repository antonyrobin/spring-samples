document.addEventListener("DOMContentLoaded", function () {

    // 1. Tagify
    document.querySelectorAll('.tagify-input').forEach(input => {
        const options = input.dataset.options ? input.dataset.options.split(',') : [];
        new Tagify(input, { whitelist: options, dropdown: { maxItems: 20, enabled: 0 } });
    });

    // 2. CKEditor
    const editors = {};
    document.querySelectorAll('.rich-editor').forEach(textarea => {
        ClassicEditor.create(textarea).then(editor => { editors[textarea.id] = editor; }).catch(console.error);
    });

    // 3. File Preview Logic (Image vs File)
    window.handleFileSelect = function(input) {
        const dropZone = input.nextElementSibling;
        const file = input.files[0];
        const isImage = input.getAttribute('accept')?.includes('image');

        if (file) {
            const infoEl = dropZone.querySelector('.file-info');
            const previewEl = dropZone.querySelector('.img-preview');

            // Image Preview
            if (isImage && file.type.startsWith('image/')) {
                const reader = new FileReader();
                reader.onload = (e) => {
                    previewEl.src = e.target.result;
                    previewEl.style.display = 'block';
                    dropZone.querySelector('.drop-icon').style.display = 'none';
                };
                reader.readAsDataURL(file);
            }

            infoEl.style.display = 'block';
            infoEl.innerHTML = `<strong>Selected:</strong> ${file.name}`;
            dropZone.classList.add('file-selected');
        }
    };

    window.triggerFileClick = function(dropZone) { dropZone.previousElementSibling.click(); };

    // 4. Client Side Validation
    const form = document.getElementById('dynamicForm');
    if(form) {
        form.addEventListener('submit', function (e) {
            let isValid = true;
            // Sync CKEditor
            Object.values(editors).forEach(ed => ed.updateSourceElement());

            // Clear errors
            document.querySelectorAll('.error-message').forEach(el => el.innerText = '');
            document.querySelectorAll('.is-invalid').forEach(el => el.classList.remove('is-invalid'));

            // Basic Loops
            const inputs = form.querySelectorAll('[data-mandatory="true"]');
            inputs.forEach(input => {
                // Radio/Checkbox special handling
                if(input.type === 'radio' || input.type === 'checkbox') {
                    // (Simpler logic: relying on container class validation or server side for strictness)
                    // For this demo, we rely on Server Side for complex radio/check valid to save JS space,
                    // but basic Text/Email/Select validation is here:
                    return;
                }

                if (!input.value.trim()) {
                    showError(input, "This field is required.");
                    isValid = false;
                }
            });

            if (!isValid) e.preventDefault();
        });
    }

    function showError(input, msg) {
        input.classList.add('is-invalid');
        const errDiv = document.getElementById('error-' + input.id);
        if(errDiv) errDiv.innerText = msg;
    }

    function showDeleteConfirm(id) {
        const modal = document.getElementById('deleteModal');
        const confirmLink = document.getElementById('confirmDeleteLink');
        modal.style.display = 'flex';
        confirmLink.href = '/delete/' + id;
    }

    function closeModal() {
        document.getElementById('deleteModal').style.display = 'none';
    }

    document.querySelectorAll('.drop-zone').forEach(dropZone => {
        // FIX: The input is a sibling (previous element), not a child
        const input = dropZone.parentElement.querySelector('input[type="file"]');

        // Drag effects
        dropZone.addEventListener('dragover', (e) => {
            e.preventDefault();
            dropZone.classList.add('drop-zone--over');
        });

        ['dragleave', 'dragend'].forEach(type => {
            dropZone.addEventListener(type, () => {
                dropZone.classList.remove('drop-zone--over');
            });
        });

        dropZone.addEventListener('drop', (e) => {
            e.preventDefault();
            dropZone.classList.remove('drop-zone--over');

            if (e.dataTransfer.files.length) {
                input.files = e.dataTransfer.files; // Sync dropped files to the hidden input
                handleFilePreview(input, dropZone); // Trigger preview logic
            }
        });

        // Handle click via the existing triggerFileClick function or direct listener
        // Note: Remove onclick="triggerFileClick(this)" from HTML to use this listener:
        dropZone.addEventListener('click', () => input.click());
    });

// Helper to handle both Image and File previews
    function handleFilePreview(input, dropZone) {
        const file = input.files[0];
        const infoEl = dropZone.querySelector('.file-info');
        const textEl = dropZone.querySelector('.drop-text');
        const iconEl = dropZone.querySelector('.drop-icon');
        const previewImg = dropZone.querySelector('.img-preview');

        if (file) {
            textEl.style.display = 'none';
            if (iconEl) iconEl.style.display = 'none';
            infoEl.style.display = 'block';
            infoEl.innerHTML = `<strong>Selected:</strong> ${file.name}`;

            // Image specific preview
            if (file.type.startsWith('image/') && previewImg) {
                const reader = new FileReader();
                reader.onload = (e) => {
                    previewImg.src = e.target.result;
                    previewImg.style.display = 'block';
                };
                reader.readAsDataURL(file);
            }
        }
    }
});