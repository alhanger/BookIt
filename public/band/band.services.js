(function() {
  'use strict';
  angular
    .module('band')
    .factory('BandService', function($http, moment, _) {

      var createband = function(newBand) {
          var url = "/create-band";
          return $http.post(url,newBand);
      };

      var getDetails = function(id) {
        var url = '/get-band/'+id;
        return $http.get(url);
      };

      var removeBand = function(band){
        var id = band.id;
        var url = "/delete-band/" + id;
        return $http.delete(url);
        };


      var updateBand = function(band) {
        var id = band.id;
        var url = '/edit-band/' + id;
        return $http.put(url, band);
      };

      var getUser = function() {
        var url = '/get-user';
        return $http.get(url);
      };

      var getNextShows = function(id) {
        var url = '/get-events/' + id;
        return $http.get(url);
      };

      var deleteShow = function (show) {
        var showId = show.id;
        var url = '/delete-event/' + showId;
        return $http.delete(url);
      };

      var confirmShow = function(show) {
        var id = show.id;
        var url = '/edit-event/' + id;
        return $http.put(url, show);
      };

      return {
        createband: createband,
        getDetails: getDetails,
        removeBand: removeBand,
        updateBand: updateBand,
        getUser: getUser,
        getNextShows: getNextShows,
        deleteShow: deleteShow,
        confirmShow: confirmShow
      };
    });


}());
