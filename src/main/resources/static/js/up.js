async function uploadSong() {
    const fileInput = document.getElementById('songFile');
    const title = document.getElementById('title').value;
    const artist = document.getElementById('artist').value;

    if (!fileInput.files.length) {
        alert('Vui lòng chọn tệp nhạc');
        return;
    }

    const formData = new FormData();
    formData.append('file', fileInput.files[0]);
    formData.append('title', title);
    formData.append('artist', artist);

    try {
        const response = await fetch('/api/music/uploadSongs', {
            method: 'POST',
            body: formData
        });

        const result = await response.text();
        alert(result);
    } catch (error) {
        console.error('Lỗi:', error);
        alert('Tải lên bài hát thất bại');
    }
}