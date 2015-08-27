$('input[id=file]').change(function() {
    var path = $(this).val();
    path = path.split('\\');
    path = path[path.length-1];
    $('#uploadurl').val(path);
});
