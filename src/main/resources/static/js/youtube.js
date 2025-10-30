let player, duration = 0, audioContext, analyser, dataArray, canvas, canvasContext;
let isPlayerReady = false;
let currentVideoId = null;

// Initialize visualizer
function initVisualizer() {
    canvas = document.getElementById('sound-visualizer');
    canvasContext = canvas.getContext('2d');

    // Create default visualization
    drawDefaultVisualizer();
}

function drawDefaultVisualizer() {
    const centerX = canvas.width / 2;
    const centerY = canvas.height / 2;

    canvasContext.clearRect(0, 0, canvas.width, canvas.height);

    // Draw concentric circles
    const time = Date.now() * 0.002;
    for (let i = 1; i <= 8; i++) {
        canvasContext.beginPath();
        const radius = 50 + i * 30 + Math.sin(time + i) * 10;
        const opacity = 0.3 - (i * 0.03);
        canvasContext.strokeStyle = `rgba(29, 185, 84, ${opacity})`;
        canvasContext.lineWidth = 2;
        canvasContext.arc(centerX, centerY, radius, 0, Math.PI * 2);
        canvasContext.stroke();
    }

    // Center dot
    canvasContext.beginPath();
    canvasContext.fillStyle = '#1DB954';
    canvasContext.arc(centerX, centerY, 8, 0, Math.PI * 2);
    canvasContext.fill();

    requestAnimationFrame(drawDefaultVisualizer);
}

function updateStatus(message, type = 'info') {
    const statusDiv = document.getElementById('status');
    statusDiv.textContent = message;
    statusDiv.className = 'status ' + type;
}

function loadVideo() {
    const url = document.getElementById('youtube-url').value.trim();
    if (!url) {
        updateStatus('Vui lòng nhập URL YouTube', 'error');
        return;
    }

    const videoId = extractVideoId(url);
    if (!videoId) {
        updateStatus('URL YouTube không hợp lệ', 'error');
        return;
    }

    currentVideoId = videoId;
    updateStatus('Đang tải video... ⏳', 'info');

    // Show video info
    document.getElementById('video-info').style.display = 'block';
    document.getElementById('video-id').textContent = `Video ID: ${videoId}`;
    // Hiển thị thumbnail
    const thumbUrl = getThumbnailUrl(videoId);
    document.getElementById('video-thumbnail').scr = thumbUrl;
    if (isPlayerReady) {
        player.loadVideoById(videoId);
    } else {
        // Initialize player if not ready
        initializePlayer(videoId);
    }
}

function extractVideoId(url) {
    const patterns = [
        /(?:v=|\/embed\/|\/watch\?v=|youtu\.be\/)([a-zA-Z0-9_-]{11})/,
        /^[a-zA-Z0-9_-]{11}$/
    ];

    for (const pattern of patterns) {
        const match = url.match(pattern);
        if (match) return match[1];
    }
    return null;
}
function getThumbnailUrl(videoId, quality = 'maxresdefault') {
    return `https://img.youtube.com/vi/${videoId}/${quality}.jpg`;
}
function initializePlayer(videoId) {
    if (window.YT && YT.Player) {
        onYouTubeIframeAPIReady();
    }
}

function onYouTubeIframeAPIReady() {
    player = new YT.Player('player', {
        height: '1',
        width: '1',
        videoId: currentVideoId || 'dQw4w9WgXcQ',
        playerVars: {
            'autoplay': 0,
            'controls': 0,
            'rel': 0,
            'modestbranding': 1,
            'playsinline': 1,
            'enablejsapi': 1
        },
        events: {
            'onReady': onPlayerReady,
            'onStateChange': onPlayerStateChange,
            'onError': onPlayerError
        }
    });
}

function onPlayerReady(event) {
    console.log('Player ready');
    isPlayerReady = true;
    updateStatus('Sẵn sàng phát nhạc! 🎵', 'success');

    // Show controls
    document.getElementById('controls').style.display = 'block';

    // Set initial volume
    player.setVolume(50);

    // Get duration and start updating
    duration = player.getDuration();
    updateTimeDisplay();

    // Update seek bar every second
    setInterval(() => {
        if (isPlayerReady) {
            updateSeek();
            updateTimeDisplay();
        }
    }, 1000);
}

function onPlayerStateChange(event) {
    console.log('Player state changed:', event.data);

    // Update duration when video loads
    if (event.data === 1 || event.data === 3) {
        duration = player.getDuration();
        updateTimeDisplay();

        // Try to get video title
        updateVideoTitle();

        if (event.data === YT.PlayerState.PLAYING) {
    const id = player.getVideoData().video_id;
    document.getElementById('video-thumbnail').src = getThumbnailUrl(id);
}

    }
}

function onPlayerError(event) {
    console.error('Player error:', event.data);
    const errors = {
        '2': 'Yêu cầu không hợp lệ',
        '5': 'Lỗi HTML5 player',
        '100': 'Video không tồn tại hoặc đã bị xóa',
        '101': 'Video không cho phép phát nhúng',
        '150': 'Video không cho phép phát nhúng'
    };
    updateStatus('Lỗi: ' + (errors[event.data] || 'Lỗi không xác định'), 'error');
}

// hàm lấy title video youtube
async function updateVideoTitle() {
//trả về nếu không có video ID
    if(!currentVideoId) return;
// tạo url yt gốc
    const url = `https://www.youtube.com/watch?v=${currentVideoId}`;
    
    //gửi yêu cần về backend
    try{
        const res = await fetch(`/youtube/title?url=${encodeURIComponent(url)}`);
        const data = await res.json(); // chờ phản hồi và chuyển dữ liệu về json

        //hiển thị title
        document.getElementById('video-title').textContent = data.title || `video ID: ${currentVideoId}`; 
    } catch (err){ // trường hợp nếu lỗi
        console.error('Lỗi lấy tiêu đề:', err);
        document.getElementById('video-title').textContent = `Video ID: ${currentVideoId}`
    }
}


function onPlayerError(event) {
    console.error('Player error:', event.data);
    const errors = {
        '2': 'Yêu cầu không hợp lệ',
        '5': 'Lỗi HTML5 player',
        '100': 'Video không tồn tại hoặc đã bị xóa',
        '101': 'Video không cho phép phát nhúng',
        '150': 'Video không cho phép phát nhúng'
    };
    updateStatus('Lỗi: ' + (errors[event.data] || 'Lỗi không xác định'), 'error');
}

function play() {
    if (isPlayerReady) {
        player.playVideo();
        updateStatus('Đang phát nhạc... 🎵', 'success');
    }
}

function pause() {
    if (isPlayerReady) {
        player.pauseVideo();
        updateStatus('Đã tạm dừng ⏸️', 'info');
        isPlaying = false;
    }
}

function stop() {
    if (isPlayerReady) {
        player.stopVideo();
        updateStatus('Đã dừng phát ⏹️', 'info');
        isPlaying = false;
    }
}

function updateSeek() {
    if (isPlayerReady && player.getCurrentTime) {
        const current = player.getCurrentTime();
        if (duration > 0) {
            const percentage = (current / duration) * 100;
            document.getElementById('seek').value = percentage;
        }
    }
}

function formatTime(seconds) {
    if (isNaN(seconds) || seconds < 0) return '0:00';
    const mins = Math.floor(seconds / 60);
    const secs = Math.floor(seconds % 60);
    return mins + ':' + (secs < 10 ? '0' : '') + secs;
}

function updateTimeDisplay() {
    if (isPlayerReady && duration > 0) {
        const current = player.getCurrentTime() || 0;
        document.getElementById('time-display').textContent =
            formatTime(current) + ' / ' + formatTime(duration);

        // Update duration display in seek bar
        document.getElementById('duration-display').textContent = formatTime(duration);

        // Update current time in seek bar
        const currentTimeSpan = document.querySelector('.slider-container span');
        if (currentTimeSpan) {
            currentTimeSpan.textContent = formatTime(current);
        }
    }
}

// Event listeners
document.getElementById('seek').addEventListener('input', function (e) {
    if (isPlayerReady && duration > 0) {
        const seekTo = (e.target.value / 100) * duration;
        player.seekTo(seekTo, true);
    }
});

document.getElementById('volume').addEventListener('input', function (e) {
    if (isPlayerReady) {
        const volume = e.target.value;
        player.setVolume(volume);
        document.getElementById('volume-display').textContent = volume + '%';
    }
});

// Enter key support for URL input
document.getElementById('youtube-url').addEventListener('keypress', function (e) {
    if (e.key === 'Enter') {
        loadVideo();
    }
});

// Initialize on page load
document.addEventListener('DOMContentLoaded', function () {
    initVisualizer();

    // Add some sample URLs as placeholder
    const sampleUrls = [
        'https://www.youtube.com/watch?v=dQw4w9WgXcQ',
        'https://www.youtube.com/watch?v=9bZkp7q19f0',
        'https://youtu.be/dQw4w9WgXcQ'
    ];

    console.log('Sample URLs you can try:', sampleUrls);
});