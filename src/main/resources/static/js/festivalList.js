// 공백을 포함해 제목은 30글자, 장소는 15글자까지만 표시한다.
function shortenText(selector, limit) {
    document.querySelectorAll(selector).forEach(function (cell) {
        const fullText = cell.textContent.trim();
        const characters = Array.from(fullText);

        if (characters.length > limit) {
            cell.textContent = characters.slice(0, limit).join('') + '...';
            cell.title = fullText;
            cell.setAttribute('aria-label', fullText);
        }
    });
}

shortenText('.festival-title', 30);
shortenText('.festival-location', 15);

// 검색 기능 연결 전에는 Enter 입력으로 페이지가 새로고침되지 않도록 한다.
document.querySelector('form[aria-label="축제 검색"]').addEventListener('submit', function (event) {
    event.preventDefault();
});
