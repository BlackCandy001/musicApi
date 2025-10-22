function showToast(message, type = "success") {
    const container = document.getElementById("toast-container");
    const toast = document.createElement("div");
    toast.className = `toast ${type}`;
    toast.innerText = message;

    container.appendChild(toast);

    // Tự remove sau khi animation kết thúc (~3.5s)
    setTimeout(() => {
        toast.remove();
    }, 3500);
}