$(document).ready(function () {
    $(".btn-delete").on("click", function (e) {
        e.preventDefault();
        link = $(this);
        recordName = link.attr("recordName");
        $("#yesBtn").attr("href", link.attr("href"));
        $("#confirmText").html("Do you want to delete the record \<strong\>" + recordName + "\<\/strong\>?");
        $("#confirmModal").modal();
    });

    $("#btnClear").on("click", function (e) {
        e.preventDefault();
        $("#keyword").text("");
        window.location = "[[@{/records}]]";
    });
});


    document.addEventListener('DOMContentLoaded', () => {
      let btnDelete = document.getElementsByName('btnDelete');
      let confirmModal = document.getElementById('confirmModal');
      
      btnDelete.forEach((b) => {
        b.addEventListener('click', (e) => {
          e.preventDefault();
          link = b.getAttribute('href');
          recordName = b.getAttribute('recordName');
          document.getElementById('yesBtn').setAttribute('href', link);
          document.getElementById('confirmText').innerHTML = "Вы действительно хотите удалить запись \<strong\>" + recordName + "\<\/strong\>?";
          confirmModal.classList.add('show');
          confirmModal.setAttribute('style', 'display:block;');
        });
      });
      let modalButtonClose = document.querySelectorAll('button[data-dismiss=\"modal\"]');
      modalButtonClose.forEach((b) =>{
        b.addEventListener('click', (e) => {
          confirmModal.classList.remove('show');
          confirmModal.removeAttribute('style');
        });
      });
      let btnClear = document.getElementById('btnClear');
      btnClear.addEventListener('click', (e) => {
        e.preventDefault;
        btnClear.text = '';
        window.location = '@{${redirectkLink}}';
      });
    });
    let alertClose = document.querySelector('#alert button');
    if(alertClose){
      alertClose.addEventListener('click',() => {
        document.getElementById('alert').remove();
      });
    }
    function changePageSize() {
      console.log('changePageSize')
      let form = document.getElementById('searchForm');
      form.submit();
    }