const dashboardGenreSegments = document.querySelector('#dashboard-genre-segments');
const dashboardGenreLegend = document.querySelector('#dashboard-genre-legend');
let dashboardGenreOffset = 0;

dashboardGenreTags.forEach(function (tag) {
    // pathLength=100으로 원의 전체 길이를 100으로 취급해 비율을 그대로 적용한다.
    const segment = document.createElementNS('http://www.w3.org/2000/svg', 'circle');
    Object.entries({
        cx: 50, cy: 50, r: 42, fill: 'none', stroke: tag.color,
        'stroke-width': 16, pathLength: 100,
        'stroke-dasharray': `${tag.percent} ${100 - tag.percent}`,
        'stroke-dashoffset': -dashboardGenreOffset,
        transform: 'rotate(-90 50 50)'
    }).forEach(function (entry) { segment.setAttribute(entry[0], entry[1]); });
    dashboardGenreSegments.append(segment);
    dashboardGenreOffset += Number(tag.percent);

    const item = document.createElement('li');
    const dot = document.createElement('span');
    dot.className = 'dashboard-legend-dot';
    dot.style.backgroundColor = tag.color;
    const name = document.createElement('span');
    name.textContent = tag.tagName;
    const percentage = document.createElement('strong');
    percentage.textContent = `${tag.percent}%`;
    item.append(dot, name, percentage);
    dashboardGenreLegend.append(item);
});