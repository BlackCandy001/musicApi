async function login() {
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    try {
        const response = await fetch('/api/auth/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: `username=${encodeURIComponent(username)}&password=${encodeURIComponent(password)}`
        });

        const result = await response.text();
        alert(result);

        if (result === 'Login successful') {
            window.location.href = '/pl';
        }
    } catch (error) {
        console.error('Lỗi:', error);
        alert('Đăng nhập thất bại');
    }
}