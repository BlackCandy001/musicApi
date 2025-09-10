let currentUserId = null;

async function loadPlaylists() {
    try {
        const response = await fetch(`/api/users/${currentUserId}/playlists`);
        const playlists = await response.json();
        renderPlaylistList(playlists);
    } catch (error) {
        console.error('Lỗi:', error);
        alert('Không thể tải playlist');
    }
}

function renderPlaylistList(playlists) {
    const playlistContainer = document.getElementById('playlists');
    playlistContainer.innerHTML = ''; 
    if (playlists.length === 0) {
        playlistContainer.innerHTML = "<p>Chưa có playlist nào.</p>";
        return;
    }

    playlists.forEach(playlist => {
        playlistContainer.appendChild(createPlaylistElement(playlist));
    });
}

function createPlaylistElement(playlist) {
    const playlistElement = document.createElement('div');
    playlistElement.classList.add('playlist-item');
    playlistElement.innerHTML = `
        <h3>${playlist.name}</h3>
        <p>Số bài hát: ${playlist.songs ? playlist.songs.length : 0}</p>
        <button onclick="viewPlaylist(${playlist.id})">Xem Playlist</button>
        <button onclick="deletePlaylist(${playlist.id})">Xóa Playlist</button>
    `;
    return playlistElement;
}

async function deletePlaylist(playlistId) {
    if (!confirm('Bạn có chắc chắn muốn xóa playlist này?')) return;

    try {
        const response = await fetch(`/api/users/${currentUserId}/playlists/${playlistId}`, {
            method: 'DELETE',
        });

        if (response.ok) {
            alert('Playlist đã được xóa.');
            loadPlaylists();
        } else {
            const error = await response.json();
            alert(`Lỗi khi xóa playlist: ${error.message}`);
        }
    } catch (error) {
        console.error('Lỗi:', error);
        alert('Không thể xóa playlist');
    }
}

async function createPlaylist() {
    const playlistName = document.getElementById('playlistName').value.trim();
    if (!playlistName) {
        alert('Vui lòng nhập tên playlist.');
        return;
    }

    try {
        const response = await fetch(`/api/users/${currentUserId}/playlists`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: `playlistName=${encodeURIComponent(playlistName)}`
        });

        if (!response.ok) throw new Error('Không thể tạo playlist');
        
        const newPlaylist = await response.json();
        alert('Playlist mới đã được tạo');
        document.getElementById('playlistName').value = '';
        loadPlaylists();
    } catch (error) {
        console.error('Lỗi:', error);
        alert('Không thể tạo playlist');
    }
}

async function viewPlaylist(playlistId) {
    try {
        const response = await fetch(`/api/users/${currentUserId}/playlists/${playlistId}`);
        const songs = await response.json();

        renderPlaylistDetails(songs);
    } catch (error) {
        console.error('Lỗi:', error);
        alert('Không thể tải danh sách bài hát');
    }
}

function renderPlaylistDetails(songs) {
    const playlistDetails = document.getElementById('playlistDetails');
    playlistDetails.innerHTML = ''; // Reset list

    if (songs.length === 0) {
        playlistDetails.innerHTML = "<p>Không có bài hát nào.</p>";
        return;
    }

    songs.forEach(song => {
        playlistDetails.appendChild(createSongElement(song));
    });
    playlistDetails.style.display = 'block';
}

function createSongElement(song) {
    const songElement = document.createElement('div');
    songElement.classList.add('song-item');
    songElement.innerHTML = `
        <h4>${song.title}</h4>
        <p>Ca sĩ: ${song.artist}</p>
        <button onclick="playSong(${song.id})">Phát bài hát</button>
    `;
    return songElement;
}

async function playSong(songId) {
    try {
        const audioElement = document.getElementById('audio-player');
        const songUrl = `/api/songs/${songId}/play`;
        audioElement.src = songUrl;
        audioElement.play();
    } catch (error) {
        console.error('Lỗi:', error);
        alert('Không thể phát bài hát');
    }
}

async function fetchCurrentUserId() {
    try {
        const response = await fetch('/api/auth/getCurrentUserId');
        const text = await response.text();
        const match = text.match(/Current User ID: (\d+)/);
        if (!match) throw new Error('Người Dùng Chưa Đăng Nhập');
        currentUserId = match[1];
    } catch (error) {
        console.error('Lỗi:', error);
        alert(error.message);
    }
}

document.addEventListener('DOMContentLoaded', async () => {
    await fetchCurrentUserId();
    await loadPlaylists();
});
