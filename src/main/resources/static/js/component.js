// HTML에는 다른 HTML을 직접 삽입하는 기능이 없어, 현재는 fetch로 공통 영역을 불러옵니다.
// 추후 Thymeleaf를 사용하면 없어질 파일입니다.
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
