const dashboardGenreSegments = document.querySelector('#dashboard-genre-segments');
const dashboardGenreLegend = document.querySelector('#dashboard-genre-legend');
let dashboardGenreOffset = 0;

const dashboardGenreTags = Array.from(
    dashboardGenreLegend.querySelectorAll('li')
).map(function (item) {
    return {
        color: item.dataset.color,
        percent: Number(item.dataset.percent)
    };
});

dashboardGenreTags.forEach(function (tag) {
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
});