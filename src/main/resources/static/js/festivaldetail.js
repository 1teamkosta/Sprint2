// 현재는 목록 HTML의 더미 데이터를 재사용한다.
// 서버 연동 시 id로 상세 API를 호출하거나 Thymeleaf 모델로 렌더링하도록 교체한다.
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
async function loadFestivalDetail() {
    const festivalId = new URLSearchParams(window.location.search).get('id') || '1';
    document.querySelector('#festival-edit-link').href = `festivalEdit.html?id=${encodeURIComponent(festivalId)}`;
    const feedback = document.querySelector('#festival-detail-feedback');
    try {
        const response = await fetch('festivals.html');
        if (!response.ok) throw new Error('축제 목록을 불러오지 못했습니다.');
        const listDocument = new DOMParser().parseFromString(await response.text(), 'text/html');
        const row = Array.from(listDocument.querySelectorAll('tbody tr[data-status]')).find(function (item) {
            return item.querySelector('[data-field="festivalId"]').textContent.trim() === festivalId;
        });
        if (!row) throw new Error('해당 축제를 찾을 수 없습니다.');

        const title = row.querySelector('[data-field="title"]').textContent.trim();
        const genre = row.querySelector('[data-field="genre"]').textContent.trim();
        const place = row.querySelector('[data-field="place"]').textContent.trim();
        document.title = `${title} | 축제메이트`;
        document.querySelector('#festival-title').textContent = title;
        document.querySelector('#festival-genre').textContent = genre;
        renderFestivalDetailTagColors();
        document.querySelector('#festival-place').textContent = place;
        // 목록에서 가져온 time 요소를 유지해 날짜의 기계 판독용 값도 보존한다.
        const schedule = document.querySelector('#festival-schedule');
        schedule.replaceChildren(...Array.from(row.querySelector('[data-sort-field="startDate"]').childNodes));
        document.querySelector('#festival-bookmark-count').textContent = row.querySelector('[data-sort-field="likeCount"]').textContent.trim();
        document.querySelector('#festival-description').textContent = `${title}에서 다양한 ${genre} 프로그램을 만나 보세요.\n${place}에서 축제메이트와 함께 즐거운 추억을 만들어 보세요.`;
        const isPublic = row.dataset.status === 'visible';
        const status = document.querySelector('#festival-status');
        status.textContent = isPublic ? '공개' : '비공개';
        status.className = `badge festival-detail-${isPublic ? 'visible' : 'hidden'}`;
        // 비공개인 축제에는 같은 상태를 적용하는 버튼을 비활성화한다.
        document.querySelector('#festival-visibility').disabled = !isPublic;
        renderFestivalProgress();
    } catch (error) {
        document.querySelector('.festival-detail-card').hidden = true;
        document.querySelector('#festival-visibility').disabled = true;
        feedback.textContent = '축제 정보를 표시할 수 없습니다. 목록으로 돌아가 다시 선택해 주세요.';
        feedback.hidden = false;
        console.error(error);
    }
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

loadFestivalDetail();
