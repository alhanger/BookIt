(function() {
  'use strict';
  angular
    .module('band')
      .directive('addBandForm', function() {
        return {
          restrict: 'E',
          templateUrl: 'band/views/addForm.html',
          link: function(scope,el,attr) {

          }
        };
      })
      .directive('upcomingShow', function() {
        return {
          restrict: 'E',
          templateUrl: 'band/views/upComingShows.html',
          controller: function($scope, $state, $stateParams, BandService) {

            $scope.remove = true;

            $scope.showSettings = function () {
              if ($scope.settings === false) {
                $scope.settings = true;
                $scope.remove = true;
              } else {
                $scope.settings = false;
                $scope.remove = false;
              }
            };

            $scope.confirmShow = function (show) {
              show.isConfirmed = true;
              BandService.confirmShow(show);
            };

            $scope.dangerZone = function() {
              $scope.danger = true;
              $scope.remove = false;
            };

            $scope.uncancel = function () {
              $scope.danger = false;
              $scope.remove = true;
            };

            $scope.cancelShow = function(show) {
              BandService.deleteShow(show);

              // put in link if possible
              var deletedShow = document.getElementById(show.id);
              deletedShow.remove();
            };

          },
          link: function(scope,el,attr) {

          }
        };
      });
}());
