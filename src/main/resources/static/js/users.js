// 회원 관리 화면도 공통 목록 기능을 사용한다.
setupList({
    prefix: 'user',
    tableSelector: '.user-table',
    idField: 'userId',
    exactFields: ['userId']
});

// 더미 데이터용 전환코드 새로고침하면 원래 상태로 돌아옴.
document.querySelectorAll('.user-suspend').forEach(function (button) {
    button.addEventListener('click', function () {
        const row = button.closest('tr');
        const nickname = row.querySelector('[data-field="nickname"]').textContent.trim();
        const suspended = row.dataset.status !== 'suspended';
        row.dataset.status = suspended ? 'suspended' : 'active';
        button.textContent = suspended ? '해제' : '정지';
        button.setAttribute('aria-label', `${nickname} 회원 ${suspended ? '정지 해제' : '정지'}`);
    });
});
