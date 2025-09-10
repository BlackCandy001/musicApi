let currentUserId = null;

async function loadSongs() {
    try {
        const response = await fetch('/api/songs');
        const songs = await response.json();
        renderSongList(songs, 'song-list', 'Không có bài hát nào.');
    } catch (error) {
        console.error('Lỗi:', error);
        alert('Không thể tải danh sách bài hát');
    }
}

function renderSongList(songs, containerId, emptyMessage) {
    const songContainer = document.getElementById(containerId);
    songContainer.innerHTML = ''; // Reset list
    if (songs.length === 0) {
        songContainer.innerHTML = `<p>${emptyMessage}</p>`;
        return;
    }

    songs.forEach(song => {
        songContainer.appendChild(createSongElement(song));
    });
}

function createSongElement(song) {
    const songElement = document.createElement('div');
    songElement.classList.add('song-item');
    songElement.innerHTML = `
        <h4>${song.title}</h4>
        <p>Ca sĩ: ${song.artist}</p>
        <button onclick="playSong(${song.id})">Phát bài hát</button>
        <button onclick="openPlaylistModal(${song.id})">Thêm vào danh sách phát</button>
    `;
    return songElement;
}

function playSong(songId) {
    const audioElement = document.getElementById('audio-player');
    const songUrl = `/api/songs/${songId}/play`;
    audioElement.src = songUrl;
    audioElement.play();
}

function openPlaylistModal(songId) {
    const modal = document.getElementById('playlist-modal');
    modal.style.display = 'block';
    window.currentSongId = songId;
}

function closeModal() {
    const modal = document.getElementById('playlist-modal');
    modal.style.display = 'none';
}

async function addSongToPlaylist(playlistId, songId) {
    try {
        const response = await fetch(`/api/users/${currentUserId}/playlists/${playlistId}/songs?songId=${songId}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
        });

        if (response.ok) {
            alert('Thêm vào danh sách phát thành công!');
            closeModal();
        } else {
            alert('Lỗi khi thêm vào danh sách phát.');
        }
    } catch (error) {
        console.error('Lỗi:', error);
    }
}

async function fetchCurrentUserId() {
    try {
        const response = await fetch('/api/auth/getCurrentUserId');
        const text = await response.text();
        const match = text.match(/Current User ID: (\d+)/);
        if (!match) throw new Error('Người dùng chưa đăng nhập');
        currentUserId = match[1];
    } catch (error) {
        console.error('Lỗi:', error);
        alert(error.message);
    }
}

async function loadPlaylists() {
    try {
        const response = await fetch(`/api/users/${currentUserId}/playlists`);
        const playlists = await response.json();
        renderPlaylistOptions(playlists);
    } catch (error) {
        console.error('Lỗi:', error);
        alert('Không thể tải danh sách playlist');
    }
}

function renderPlaylistOptions(playlists) {
    const playlistContainer = document.getElementById('playlist-options');
    playlistContainer.innerHTML = ''; // Reset list
    if (playlists.length === 0) {
        playlistContainer.innerHTML = "<p>Không có danh sách phát nào.</p>";
        return;
    }

    playlists.forEach(playlist => {
        const option = document.createElement('div');
        option.innerHTML = `
            <p>${playlist.name}</p>
            <button onclick="addSongToPlaylist(${playlist.id}, window.currentSongId)">Thêm</button>
        `;
        playlistContainer.appendChild(option);
    });
}

async function searchSong() {
    const titleInput = document.getElementById('searchSong');
    const title = titleInput.value.trim();

    if (!title) {
        alert('Vui lòng nhập tên bài hát để tìm kiếm.');
        return;
    }

    try {
        const response = await fetch(`/api/songs/search?title=${encodeURIComponent(title)}`);
        const songs = await response.json();
        renderSongList(songs, 'song-list', 'Không tìm thấy bài hát nào.');
    } catch (error) {
        console.error('Lỗi:', error);
        alert('Không thể tìm kiếm bài hát.');
    }
}

document.addEventListener('DOMContentLoaded', async () => {
    await fetchCurrentUserId();
    await loadPlaylists();
    await loadSongs();
});
