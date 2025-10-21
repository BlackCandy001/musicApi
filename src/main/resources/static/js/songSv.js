let currentUserId = null;

async function loadSongs() {
    try {
        const response = await fetch('/api/songs');
        const songs = await response.json();
        renderSongList(songs, 'song-list', 'Không có bài hát nào.');
    } catch (error) {
        console.error('Lỗi:', error);
        showToast('Không thể tải danh sách bài hát', 'error');
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
        <div class="song-meta">
            <h4 class="song-title">${song.title}</h4>
            <p class="song-artist">${song.artist}</p>
        </div>
        <div class="song-actions">
            <button class="btn-circle" onclick="playSong(${song.id})">►</button>
            <button class="btn-circle" onclick="openPlaylistModal(${song.id})">＋</button>
        </div>
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
    loadPlaylists();
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
            showToast('Thêm vào danh sách phát thành công!', 'success');
            closeModal();
        } else {
            showToast('Lỗi khi thêm vào danh sách phát.', 'error');
        }
    } catch (error) {
        console.error('Lỗi:', error);
    }
}

// kiểm tra đăng nhập (change)
async function fetchCurrentUserId() {
    try {
        const response = await fetch('/api/auth/getCurrentUserId');
        const text = await response.text();

        const match = text.match(/Current User ID: (\d+)/);

        if (!match) throw new Error('Người dùng chưa đăng nhập');

        currentUserId = match[1];
        showToast('Đăng nhập thành công', 'success', 5000);

        console.log('User ID:', currentUserId);

    } catch (error) {
        console.error('Lỗi:', error);
        showToast(error.message, 'error');
    }
}


async function loadPlaylists() {
        if (!currentUserId) {
        console.warn("Chưa có user ID, không thể tải playlist");
        return;
    } // khóa an toàn cho loand playlist
    try {
        const response = await fetch(`/api/users/${currentUserId}/playlists`);
        const playlists = await response.json();
        renderPlaylistOptions(playlists);
    } catch (error) {
        console.error('Lỗi:', error);
        showToast('Không thể tải danh sách playlist', 'error', 5000);
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
    option.classList.add('playlist-option');
    option.textContent = playlist.name;
    option.style.cursor = 'pointer';

    //css
    option.style.backgroundColor = '#4CAF50';
    option.style.border = '2px solid #000';
    option.style.padding = '10px';
    option.style.margin = '5px 0';
    option.style.cursor = 'pointer';
    option.style.borderRadius = '5px';
    option.style.color = 'white';
    option.style.fontWeight = 'bold';

    //sự kiện click
    option.addEventListener('click', () => {
        addSongToPlaylist(playlist.id, window.currentSongId);
    });
    
    playlistContainer.appendChild(option);
});
}

async function searchSong() {
    const titleInput = document.getElementById('searchSong');
    const title = titleInput.value.trim();

    if (!title) {
        showToast('Vui lòng nhập tên bài hát để tìm kiếm.','error');
        return;
    }

    try {
        const response = await fetch(`/api/songs/search?title=${encodeURIComponent(title)}`);
        const songs = await response.json();
        renderSongList(songs, 'song-list', 'Không tìm thấy bài hát nào.');
    } catch (error) {
        console.error('Lỗi:', error);
        showToast('Không thể tìm kiếm bài hát.','error');
    }
}

document.addEventListener('DOMContentLoaded', async () => {
    await fetchCurrentUserId();
    await loadPlaylists(); // làm mới playlist
    await loadSongs();
    const audio = document.getElementById("audio-player");
   // const canvas = document.getElementById("sound-visualizer"); //hiệu ứng
    //const ctx = canvas.getContext("2d");
    //let audioCtx, analyser, dataArray;
    //let isPlaying = false, animationTime = 0, visualStyle = 0;

    // Khởi tạo âm thanh
    function initAudio() {
        if (!audioCtx) {
            audioCtx = new (window.AudioContext || window.webkitAudioContext)();
            analyser = audioCtx.createAnalyser();
            analyser.fftSize = 512;
            analyser.smoothingTimeConstant = 0.8;
            const source = audioCtx.createMediaElementSource(audio);
            source.connect(analyser);
            analyser.connect(audioCtx.destination);
            dataArray = new Uint8Array(analyser.frequencyBinCount);
        }
    }


    audio.addEventListener("play", () => {
        initAudio();
        isPlaying = true;
        draw();
        audioCtx.resume();
    });
    audio.addEventListener("pause", () => { isPlaying = false; });
});