// HTML에는 다른 HTML을 직접 삽입하는 기능이 없어, 현재는 fetch로 공통 영역을 불러온다.
// 추후 Thymeleaf의 th:replace를 적용하면 이 로딩 코드는 제거할 수 있다.
async function loadFragment(containerId, filePath) {
    const container = document.getElementById(containerId);

    try {
        const response = await fetch(filePath);
        if (!response.ok) {
            throw new Error(`공통 영역 불러오기 실패: ${response.status}`);
        }
        container.innerHTML = await response.text();
    } catch (error) {
        const message = document.createElement('p');
        message.className = 'alert alert-danger m-3';
        message.setAttribute('role', 'alert');
        message.textContent = '공통 메뉴를 불러오지 못했습니다. 서버 주소로 접속했는지 확인해 주세요.';
        container.appendChild(message);
        console.error(error);
    }
}

loadFragment('sidebar-container', '../component/sidebar.html');
loadFragment('header-container', '../component/header.html');

// 검색 기능 연결 전에는 Enter 입력으로 페이지가 새로고침되지 않도록 한다.
document.querySelector('form[aria-label="축제 검색"]').addEventListener('submit', function (event) {
    event.preventDefault();
});
