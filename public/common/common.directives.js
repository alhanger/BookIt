(function() {
  'use strict';
  angular
    .module('bookit')
    .directive('navBar', function() {
      return {
        restrict: 'E',
        templateUrl: 'common/views/navigation.html',
        transclude: true,
        controller: function($scope) {

        },
        link: function(scope,element,attr) {


        }

      };
    })

    .directive('footer', function(){
      return {
        restrict: 'E',
        templateUrl: 'common/views/footer.html',
        transclude:true,

      };
    })
    .directive('sidePanel', function() {
      return {
        restrict: 'E',
        templateUrl: 'common/views/sidePanel.html',
        link: function(scope,el,attr) {

        $('.fa-bars').on('click', function(event) {
          event.preventDefault();
          if ($('.sidepanel').hasClass('slider')) {
            $('.sidepanel').addClass('back');
            $('.sidepanel').removeClass('slider');
          } else {
            $('.sidepanel').addClass('slider');
          }


        });


          // var height = $(window).height()+ 95;
          // $('.sidepanel').css('height', height);


        }
      };
    });
}());
