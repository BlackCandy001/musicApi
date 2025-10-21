async function register(event) {
    event.preventDefault();
    const username = document.getElementById('reg-username').value;
    const password = document.getElementById('reg-password').value;
    const email = document.getElementById('reg-email').value;
    try {
        const response = await fetch('/api/users/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                username: username,
                password: password,
                email: email
            })
        });

        const user = await response.json();
        alert('Đăng ký thành công! ID người dùng: ' + user.id);
        window.location.href = '/home';
    } catch (error) {
        console.error('Lỗi:', error);
        alert('Đăng ký thất bại');
    }
}