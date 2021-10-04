var navigationWrapper = $('.cd-main-nav-wrapper'),
    navigation = navigationWrapper.children('.cd-main-nav'),
    searchForm = $('.cd-main-search'),
    navigationTrigger = $('.cd-nav-trigger'),
    mainHeader = $('.cd-main-header');
function moveNavigation(){
    var screenSize = checkWindowWidth(); //returns 'mobile' or 'desktop'
    if ( screenSize == 'desktop' && (navigationTrigger.siblings('.cd-main-search').length == 0) ) {
        //desktop screen - insert navigation and search form inside <header>
        searchForm.detach().insertBefore(navigationTrigger);
        navigationWrapper.detach().insertBefore(searchForm).find('.cd-serch-wrapper').remove();
    } else if( screenSize == 'mobile' && !(mainHeader.children('.cd-main-nav-wrapper').length == 0)) {
        //mobile screen - move navigation and search form after .cd-main-content element
        navigationWrapper.detach().insertAfter('.cd-main-content');
        var newListItem = $('<li class="cd-serch-wrapper"></li>');
        searchForm.detach().appendTo(newListItem);
        newListItem.appendTo(navigation);
    }
}
