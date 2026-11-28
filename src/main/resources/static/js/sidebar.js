    const currentPage = document.body.dataset.adminPage;

    document.querySelectorAll('.admin-sidebar [data-page]').forEach(function (link) {
    const active = link.dataset.page === currentPage;

    link.classList.toggle('active', active);

    if (active) link.setAttribute('aria-current', 'page');
    else link.removeAttribute('aria-current');
});
