// HTML에는 다른 HTML을 직접 삽입하는 기능이 없어, 현재는 fetch로 공통 영역을 불러옵니다.
// 추후 Thymeleaf를 사용하면 없어질 파일입니다.
async function loadFragment(containerId, filePath) {
    const container = document.querySelector(`#${containerId}`);

    try {
        const response = await fetch(filePath);
        if (!response.ok) {
            throw new Error(`공통 영역 불러오기 실패: ${response.status}`);
        }
        container.innerHTML = await response.text();
        // 메뉴 조각의 위치를 기준으로 링크를 해석해 루트/하위 화면에서 모두 이동되게 한다.
        const fragmentUrl = new URL(filePath, document.baseURI);
        container.querySelectorAll('a[href]').forEach(function (link) {
            const href = link.getAttribute('href');
            if (href && !href.startsWith('#')) link.href = new URL(href, fragmentUrl).href;
        });
        if (containerId === 'sidebar-container') {
            const currentPage = document.body.dataset.adminPage || 'festival';
            container.querySelectorAll('[data-page]').forEach(function (link) {
                const isCurrentPage = link.dataset.page === currentPage;
                link.classList.toggle('active', isCurrentPage);
                if (isCurrentPage) {
                    link.setAttribute('aria-current', 'page');
                } else {
                    link.removeAttribute('aria-current');
                }
            });
        }
    } catch (error) {
        const message = document.createElement('p');
        message.className = 'alert alert-danger m-3';
        message.setAttribute('role', 'alert');
        message.textContent = '공통 메뉴를 불러오지 못했습니다. 서버 주소로 접속했는지 확인해 주세요.';
        container.appendChild(message);
        console.error(error);
    }
}

// 인덱스는 templates 바로 아래, 목록은 하위 폴더라 공통 영역까지의 상대 경로가 다르다.
const componentBase = document.body.dataset.componentBase || '../component/';
loadFragment('sidebar-container', `${componentBase}sidebar.html`);
loadFragment('header-container', `${componentBase}header.html`);
