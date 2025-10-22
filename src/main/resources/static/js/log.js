async function login(event) {
    event.preventDefault();

    const username = document.getElementById('log-username').value;
    const password = document.getElementById('log-password').value;

    try {
        const response = await fetch('/api/auth/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: `username=${encodeURIComponent(username)}&password=${encodeURIComponent(password)}`,
            credentials: 'include' // để lưu cookie phiên đăng nhập
        });

        const result = await response.json();

        if (response.ok && result.status === 'success') {
            // Đăng nhập thành công => chuyển tra
            showToast(result.message, 'success');
            window.location.href = "/song";
        } else {
            // Đăng nhập thất bại => in thông báo lỗi
            alert(result.message);
            // Optionally: highlight field với lỗi, nếu cần
            if (result.field) {
                const fieldElement = document.getElementById(`log-${result.field}`);
                if (fieldElement) {
                    fieldElement.classList.add('input-error');
                }
            }
        }
    } catch (error) {
        console.error('Lỗi:', error);
        alert('Có lỗi xảy ra khi gửi yêu cầu đăng nhập');
    }
}
