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
            showToast('Thêm vào danh sách phát thành công!', 'error');
            closeModal();
        } else {
            showToast('Lỗi khi thêm vào danh sách phát.', 'error');
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
        showToast(error.message, 'error');
    }
}

async function loadPlaylists() {
    try {
        const response = await fetch(`/api/users/${currentUserId}/playlists`);
        const playlists = await response.json();
        renderPlaylistOptions(playlists);
    } catch (error) {
        console.error('Lỗi:', error);
        showToast('Không thể tải danh sách playlist', 'error');
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
    await loadPlaylists();
    await loadSongs();
    const audio = document.getElementById("audio-player");
    const canvas = document.getElementById("sound-visualizer");
    const ctx = canvas.getContext("2d");
    let audioCtx, analyser, dataArray;
    let isPlaying = false, animationTime = 0, visualStyle = 0;

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

    function draw() {
        if (!isPlaying) return;
        requestAnimationFrame(draw);
        analyser.getByteFrequencyData(dataArray);

        ctx.clearRect(0, 0, canvas.width, canvas.height);
        const cx = canvas.width / 2, cy = canvas.height / 2;
        animationTime += 0.02;

        drawCircularBars(cx, cy); // bạn có thể đổi sang drawWaveCircle, drawParticleRing…

        drawCenterGlow(cx, cy);
    }

    function drawCircularBars(cx, cy) {
        const radius = 120, barCount = 128;
        for (let i = 0; i < barCount; i++) {
            const angle = (i / barCount) * Math.PI * 2;
            const idx = Math.floor((i / barCount) * dataArray.length);
            const barHeight = (dataArray[idx] || 0) * 0.8 + 10;
            const innerX = cx + Math.cos(angle) * radius;
            const innerY = cy + Math.sin(angle) * radius;
            const outerX = cx + Math.cos(angle) * (radius + barHeight);
            const outerY = cy + Math.sin(angle) * (radius + barHeight);

            const hue = (i / barCount) * 360 + animationTime * 50;
            const gradient = ctx.createLinearGradient(innerX, innerY, outerX, outerY);
            gradient.addColorStop(0, `hsla(${hue},70%,50%,0.8)`);
            gradient.addColorStop(1, `hsla(${hue+60},70%,70%,1)`);

            ctx.beginPath();
            ctx.moveTo(innerX, innerY);
            ctx.lineTo(outerX, outerY);
            ctx.strokeStyle = gradient;
            ctx.lineWidth = 4;
            ctx.lineCap = "round";
            ctx.shadowBlur = 15;
            ctx.shadowColor = `hsla(${hue},70%,50%,0.8)`;
            ctx.stroke();
        }
    }

    function drawCenterGlow(cx, cy) {
        let avg = dataArray.reduce((a, b) => a + b, 0) / dataArray.length;
        const pulse = 30 + (avg / 255) * 40;
        const gradient = ctx.createRadialGradient(cx, cy, 0, cx, cy, pulse);
        gradient.addColorStop(0, "rgba(255,255,255,0.8)");
        gradient.addColorStop(0.5, "rgba(29,185,84,0.6)");
        gradient.addColorStop(1, "rgba(29,185,84,0)");
        ctx.beginPath();
        ctx.arc(cx, cy, pulse, 0, Math.PI * 2);
        ctx.fillStyle = gradient;
        ctx.fill();

        ctx.beginPath();
        ctx.arc(cx, cy, 15, 0, Math.PI * 2);
        ctx.fillStyle = "#fff";
        ctx.shadowBlur = 20;
        ctx.shadowColor = "#fff";
        ctx.fill();
    }

    audio.addEventListener("play", () => {
        initAudio();
        isPlaying = true;
        draw();
        audioCtx.resume();
    });
    audio.addEventListener("pause", () => { isPlaying = false; });
});


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