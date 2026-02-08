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
            document.getElementById("form-validation-message").innerText = "";
            try {
                // Basic Loops
                const formGroups = form.querySelectorAll('.form-group');
                formGroups.forEach(formGroup => {
                    let hasValue = false;
                    let inputValue = "";
                    let input = null;
                    const inputType = formGroup.getAttribute("type");
                    switch (inputType) {
                        case 'radio':
                            input = formGroup.querySelector('.radio-group-container');
                            inputValue = formGroup.querySelectorAll('input[type="radio"]:checked').length > 0 ? "hasValue" : "";
                            hasValue = !!inputValue;
                            break;
                        case 'checkbox':
                            input = formGroup.querySelector('.radio-group-container');
                            inputValue = formGroup.querySelectorAll('input[type="checkbox"]:checked').length > 0 ? "hasValue" : "";
                            hasValue = !!inputValue;
                            break;
                        case 'file':
                        case 'image':
                            input = formGroup.querySelector('input[type="file"]');
                            const hasNewFile = input.files.length > 0;
                            const hasExistingFile = input.getAttribute("data-existing") === "true";
                            hasValue = hasNewFile || hasExistingFile;
                            break;
                        case 'select':
                            input = formGroup.querySelector('select');
                            inputValue = input.value.trim();
                            hasValue = !!inputValue;
                            break;
                        default:
                            input = formGroup.querySelector('input, textarea');
                            if (input) {
                                inputValue = input.value.trim();
                                hasValue = inputValue !== "";
                            }
                    }

                    if (formGroup.getAttribute("data-mandatory") === "true") {
                        if (!hasValue) {
                            showError(input, "This field is required.");
                            isValid = false;
                        }
                    }
                    // Additional validations
                    if (hasValue) {
                        if (input.type === 'file') {
                            const file = input.files[0];
                            if (file && file.size > 10 * 1024 * 1024) { // 10MB
                                showError(input, "File is too large! Please upload a file smaller than 10MB.");
                                isValid = false;
                            }
                        }
                    }
                });
                if (!isValid) {
                    document.getElementById("form-validation-message").innerText = "Please correct the errors highlighted";
                    e.preventDefault();
                }
            } catch (err) {
                console.error("Validation error:", err);
                document.getElementById("form-validation-message").innerText = "An unexpected error occurred during validation. Please try again.";
                isValid = false;
                e.preventDefault();
            }
        });
    }

    function showError(input, msg) {
        input.classList.add('is-invalid');
        const errDiv = document.getElementById('error-' + input.id);
        if(errDiv) errDiv.innerText = msg;
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