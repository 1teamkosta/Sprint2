/**
 * 대시보드 장르 분포의 색상과 비율은 태그 데이터로 관리한다.
 * 현재는 더미 데이터이며, 서버 연동 시 태그의 id·name·color와 집계 비율을 받아 교체한다.
 * 태그 색상은 공통 CSS 변수에 등록하거나 브라우저 저장소에 저장하지 않는다.
 */
const dashboardGenreTags = [
    { id: 1, name: '음악', color: '#FADADD', percentage: 42 },
    { id: 3, name: '먹거리', color: '#FFE0B2', percentage: 24 },
    // 전시는 태그 관리의 예술 태그 색상, 기타는 팔레트의 회청색을 사용한다.
    { id: 18, name: '전시', color: '#F1F4B8', percentage: 18 },
    { id: 19, name: '기타', color: '#E2E7ED', percentage: 16 }
];
const dashboardGenreSegments = document.querySelector('#dashboard-genre-segments');
const dashboardGenreLegend = document.querySelector('#dashboard-genre-legend');
let dashboardGenreOffset = 0;
dashboardGenreTags.forEach(function (tag) {
    // pathLength=100으로 원의 전체 길이를 100으로 취급해 비율을 그대로 적용한다.
    const segment = document.createElementNS('http://www.w3.org/2000/svg', 'circle');
    Object.entries({
        cx: 50, cy: 50, r: 42, fill: 'none', stroke: tag.color,
        'stroke-width': 16, pathLength: 100,
        'stroke-dasharray': `${tag.percentage} ${100 - tag.percentage}`,
        'stroke-dashoffset': -dashboardGenreOffset,
        transform: 'rotate(-90 50 50)'
    }).forEach(function (entry) { segment.setAttribute(entry[0], entry[1]); });
    dashboardGenreSegments.append(segment);
    dashboardGenreOffset += tag.percentage;

    const item = document.createElement('li');
    const dot = document.createElement('span');
    dot.className = 'dashboard-legend-dot';
    dot.style.backgroundColor = tag.color;
    dot.setAttribute('aria-hidden', 'true');
    const name = document.createElement('span');
    name.textContent = tag.name;
    const percentage = document.createElement('strong');
    percentage.textContent = `${tag.percentage}%`;
    item.append(dot, name, percentage);
    dashboardGenreLegend.append(item);
});
document.querySelector('#dashboard-genre-chart').setAttribute('aria-label',
    `예시 장르 태그 분포: ${dashboardGenreTags.map(function (tag) { return `${tag.name} ${tag.percentage}%`; }).join(', ')}, 전체 축제 248개`);

// 관심사 이름도 태그 관리와 같은 배경색·짙은 글자색으로 표시한다.
// 전시는 태그 관리의 예술 태그 색상을 사용한다. 실제 연동 시 태그 응답의 color로 교체한다.
const dashboardInterestColors = {
    '음악': '#FADADD', '먹거리': '#FFE0B2', '전시': '#F1F4B8',
    '도시': '#FBE5D6', '체험': '#DCEAF5'
};
document.querySelectorAll('[data-interest-tag]').forEach(function (badge) {
    const color = dashboardInterestColors[badge.dataset.interestTag];
    if (!color) return;
    badge.style.backgroundColor = color;
    badge.style.color = '#' + [1, 3, 5].map(function (offset) {
        return Math.round(parseInt(color.slice(offset, offset + 2), 16) * 0.3)
            .toString(16).padStart(2, '0');
    }).join('').toUpperCase();
});
