// 입력 검증을 위한 화면용 코드. 서버 연동 시 검증 후 공지 등록 API를 호출한다.
const noticeAddForm = document.querySelector('#notice-add-form');
noticeAddForm.addEventListener('submit', function (event) {
    event.preventDefault();
    const title = document.querySelector('#notice-title');
    const content = document.querySelector('#notice-content');
    title.value = title.value.trim();
    content.value = content.value.trim();
    if (!noticeAddForm.reportValidity()) return;

    const feedback = document.querySelector('#notice-save-feedback');
    feedback.textContent = '입력 확인이 완료되었습니다. 실제 공지 등록은 서버 연동 후 가능합니다.';
    feedback.hidden = false;
});
