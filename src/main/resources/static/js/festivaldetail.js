// 상세 데이터는 컨트롤러가 Thymeleaf 모델로 전달한다.
// 태그 관리와 같은 더미 색상. 서버 연동 시 각 태그의 color 값으로 교체한다.
const festivalDetailTagColors = {
    '음악': '#FADADD', '먹거리': '#FFE0B2', '문화': '#FFF0AD',
    '전통문화': '#FFF0AD', '가족': '#E6DDD2', '친구': '#F2D9EB', '연인': '#F5DEDE'
};
function renderFestivalDetailTagColors() {
    document.querySelectorAll('.festival-detail-tag').forEach(function (badge) {
        const color = festivalDetailTagColors[badge.textContent.trim()] || '#E2E7ED';
        // 태그 데이터는 CSS 변수에 등록하지 않고 배지에 직접 적용한다.
        badge.style.backgroundColor = color;
        badge.style.color = '#' + [1, 3, 5].map(function (offset) {
            return Math.round(parseInt(color.slice(offset, offset + 2), 16) * 0.3)
                .toString(16).padStart(2, '0');
        }).join('').toUpperCase();
    });
}
function renderFestivalProgress() {
    const times = document.querySelectorAll('#festival-schedule time');
    const badge = document.querySelector('#festival-progress-status');
    if (!badge || times.length < 2) return;

    const now = new Date();
    const today = `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}-${String(now.getDate()).padStart(2, '0')}`;
    const start = times[0].getAttribute('datetime');
    const end = times[1].getAttribute('datetime');

    const progress = today < start ? 'upcoming' : today > end ? 'ended' : 'ongoing';
    badge.textContent = { upcoming: '예정', ongoing: '진행중', ended: '종료' }[progress];
    badge.className = `badge festival-progress-${progress}`;
}

renderFestivalDetailTagColors();
renderFestivalProgress();
