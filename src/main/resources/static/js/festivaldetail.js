// 상세 데이터는 컨트롤러가 Thymeleaf 모델로 전달한다.
// 태그 관리와 같은 더미 색상. 서버 연동 시 각 태그의 color 값으로 교체한다.
const visibilityBtn = document.getElementById('festival-visibility');
const statusBadge = document.getElementById('festival-status'); // 상단 상태 배지 가져오기

visibilityBtn.addEventListener('click', function () {
    const url = this.dataset.url;

    fetch(url, {
        method: 'PUT'
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('서버 응답 에러: ' + response.status);
            }

            // 현재 버튼 글자가 '비공개'였다면 -> 이제 '비공개' 상태로 바뀐다는 의미
            const isNowPrivate = (this.textContent.trim() === '비공개');

            // 1. 버튼 글자 변경 ('비공개' <-> '공개')
            this.textContent = isNowPrivate ? '공개' : '비공개';

            // 2. 상단 배지(#festival-status) 글자 및 색상 클래스 변경 (버튼과 반대로 표시)
            if (statusBadge) {
                statusBadge.textContent = isNowPrivate ? '비공개' : '공개';
                statusBadge.className = `badge festival-detail-${isNowPrivate ? 'hidden' : 'visible'}`;
            }
        })
        .catch(error => {
            console.error(error);
            alert('상태 변경에 실패했습니다.');
        });
});

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
