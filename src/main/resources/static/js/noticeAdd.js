// 공지 등록·수정 폼을 Notice REST API에 연결한다.
// 보안 로그인 연결 전까지 사용할 임시 작성자 계정 일련번호.
const noticeAuthorNumber = 1;
const noticeForm = document.querySelector('#notice-add-form');
const noticeTitle = document.querySelector('#notice-title');
const noticeContents = document.querySelector('#notice-content');
const noticeFeedback = document.querySelector('#notice-save-feedback');
const noticeMode = document.body.dataset.noticeMode || 'add';
const noticeNumber = document.body.dataset.noticeNumber;

noticeForm.addEventListener('submit', function (event) {
    event.preventDefault();
    noticeTitle.value = noticeTitle.value.trim();
    noticeContents.value = noticeContents.value.trim();
    if (!noticeForm.reportValidity()) return;

    const notice = {
        title: noticeTitle.value,
        contents: noticeContents.value,
        author: noticeAuthorNumber
    };
    let url = '/admin/notice';
    let method = 'POST';

    if (noticeMode === 'edit') {
        notice.noticeNumber = Number(noticeNumber);
        method = 'PUT';
    }

    noticeFeedback.textContent = noticeMode === 'edit' ? '공지를 수정하고 있습니다.' : '공지를 등록하고 있습니다.';
    noticeFeedback.hidden = false;
    fetch(url, {
            method: method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(notice)
        }).then(function (response) {
        if (!response.ok) {
            throw new Error(`공지 저장에 실패했습니다. (${response.status})`);
        }

        if (noticeMode === 'edit') {
            window.location.href = `/admin/noticeDetailView/${encodeURIComponent(noticeNumber)}`;
        } else {
            window.location.href = '/admin/noticesView';
        }
    }).catch(function (error) {
        noticeFeedback.textContent = error.message;
        noticeFeedback.hidden = false;
        console.error(error);
    });
});
