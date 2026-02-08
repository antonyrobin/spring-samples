document.addEventListener("DOMContentLoaded", function () {

    window.showDeleteConfirm = function (id) {
        const modal = document.getElementById('deleteModal');
        const confirmLink = document.getElementById('confirmDeleteBtn');
        modal.style.display = 'flex';
        confirmLink.href = '/delete/' + id;
    }

    window.closeModal = function () {
        document.getElementById('deleteModal').style.display = 'none';
    }

    window.onclick = function(event) {
        const modal = document.getElementById('deleteModal');
        if (event.target == modal) {
            closeModal();
        }
    }
});