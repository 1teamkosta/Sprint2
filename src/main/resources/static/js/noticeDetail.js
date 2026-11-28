// 공지 상세 화면의 삭제 동작을 Notice REST API에 연결한다.
const noticeNumber = document.body.dataset.noticeNumber;
const noticeDeleteButton = document.querySelector('#notice-delete');
const noticeDetailFeedback = document.querySelector('#notice-detail-feedback');

noticeDeleteButton.addEventListener('click', function () {
    if (!confirm('이 공지를 삭제할까요?')) return;

    fetch(`/admin/notice/${encodeURIComponent(noticeNumber)}`, {
            method: 'DELETE'
        }).then(function (response) {
        if (!response.ok) {
            throw new Error(`공지 삭제에 실패했습니다. (${response.status})`);
        }
        window.location.href = '/admin/noticesView';
    }).catch(function (error) {
        noticeDetailFeedback.textContent = error.message;
        noticeDetailFeedback.hidden = false;
        console.error(error);
    });
});
