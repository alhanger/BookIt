(function() {
  'use strict';
  angular
  .module('events')
    .directive('venueBlock', function() {
      return {
        restrict: 'E',
        templateUrl: 'events/views/venueBlock.html',
        link: function(scope,el,attr) {

        },
        controller: function($scope, $state, $stateParams, EventsService) {

          $scope.details = false;

          $scope.showDetails = function() {
            if ($scope.details === false) {
              $scope.details = true;
            } else if ($scope.details === true) {
              $scope.details = false;
            }
          };

          $scope.viewSchedule = function(venue) {
            var id = venue.id;
            var bandId = $stateParams.bandId;
            var name = venue.displayName;
            $state.go('^.showcalendar',{venueId: id, bandId: bandId, venueName: name});
          };

        }
      };
    })
    .directive('calendar', function() {
      return {
        restrict: 'E',
        templateUrl: 'events/views/calendar.html',
        controller: function(EventsService, $scope, $stateParams) {

          var setVenData = function() {
            var id = $stateParams.venueId;
            EventsService.getVenDetails(id).then(function(data) {

              $scope.venAddress = data.data.street + ' ' + data.data.city.displayName
                                  + ', ' + data.data.city.state.displayName;
              $scope.venPhon = data.data.phone || 'no phone number available';
              $scope.venSite = data.data.website || 'no website available';
            });
          };

          setVenData();

          var formatMonth = function(shows, days) {

            // time stamp to check for the current day
            var now = new Date();
            // assign shows to days that match the date
            for (var i = 0; i < shows.length; i++) {
              for (var j in days) {
                // check if the day has a show
                if (days[j].standard.includes(shows[i].start.date)) {
                  days[j].show = shows[i];
                  days[j].available = false;
                } else if (days[j].long <= now.getDate()) {
                  days[j].available = false;
                }
                // assign an id to the first of the month
                // to shift the block over to the right
                if (days[j].short === '1st') {
                  if (days[j].ofWeek === 'Sunday') {
                    days[j].DOMid = 'Sunday';
                  } else if (days[j].ofWeek === 'Monday') {
                    days[j].DOMid = 'Monday';
                  } else if (days[j].ofWeek === 'Tuesday') {
                    days[j].DOMid = 'Tuesday';
                  } else if (days[j].ofWeek === 'Wednesday') {
                    days[j].DOMid = 'Wednesday';
                  } else if (days[j].ofWeek === 'Thursday') {
                    days[j].DOMid = 'Thursday';
                  } else if (days[j].ofWeek === 'Friday') {
                    days[j].DOMid = 'Friday';
                  } else if (days[j].ofWeek === 'Saturday') {
                    days[j].DOMid = 'Saturday';
                  }
                }
              } //end of j loop
            } // end of i loop
          };

          var setDays = function (date, month) {
            var days = [];
            while (date.getMonth() === month) {
              var day = {};
              day.long = new Date(date);
              day.clickable = false;
              day.dateFormat = moment(day.long).format();
              day.med = moment(day.long).format('MMM Do YYYY');
              day.short = moment(day.long).format('Do');
              day.day = moment(day.long).format('dddd');
              day.ofWeek = moment(day.long).format('dddd');
              day.month = moment(day.long).format('MMMM');
              day.monthNum = moment(day.long).format('M');
              day.standard = moment(day.long).format();
              days.push(day);
              date.setDate(date.getDate() + 1);
            }

            var id = $stateParams.venueId;
            EventsService.getCalendar(id).success(function(shows){
              $scope.shows = shows;
              formatMonth(shows, days);
            });
              $scope.month = days[0].month;
              $scope.venueName = $stateParams.venueName;
              $scope.days = days;
          };

          $scope.currMonth = function () {
            var date = new Date();
            var year = date.getFullYear();
            var month = date.getMonth();
            $scope.date = new Date(year, month, 1);
            setDays($scope.date, month);
          };

          $scope.nextMonth = function() {
            var m = $scope.date.getMonth();
            setDays($scope.date, m);
          };

          $scope.prevMonth = function() {

            if ($scope.date.getMonth() === 1) {
              var m = 11;
              var y = $scope.date.getFullYear() - 1;
              $scope.date.setMonth(m, 1);
              $scope.date.setFullYear(y);
              setDays($scope.date, m);

            } else if ($scope.date.getMonth() === 0) {
              var m = 10;
              var y = $scope.date.getFullYear() - 1;
              $scope.date.setMonth(m, 1);
              $scope.date.setFullYear(y);
              setDays($scope.date, m);

            } else {
              var m = $scope.date.getMonth() - 2;
              $scope.date.setMonth(m, 1);
              setDays($scope.date, m);
            }
          };

        }, //end of controller
        link: function(scope,el,attr) {
          $('.navbar').scrollTop(0);
        }
      };
    })
    .directive('dayBlock', function() {
      return {
        restrict: 'E',
        templateUrl: 'events/views/dayBlock.html',
        transclude: true,
        controller: function($scope, EventsService, MainService, $stateParams) {
          $scope.check = function(day) {
            $scope.day = day;
          };

          $scope.book = function (day) {
            day.available = false;
            var venId = $stateParams.venueId;
            EventsService.getVenDetails(venId).then(function(details) {
              var venDeets = details.data;
              var gig = {
                date: day.med,
                dateFormat: day.dateFormat,
                dateYear: day.long.getFullYear(),
                dateMonth: day.long.getMonth() + 1,
                dateDay: day.long.getDate(),
                venueName: $stateParams.venueName,
                venueAddress: venDeets.street + ' ' + venDeets.city.displayName + ', ' + venDeets.city.state.displayName + ' ' + venDeets.zip,
                venuePhoneNum: venDeets.phone || 'no phone number available',
                venueWebsite: venDeets.website || 'no website available',
                venCapacity: venDeets.capacity || 'capacity info not available',
                venueLong: venDeets.lng,
                venueLat: venDeets.lat
              };
              var bandId = $stateParams.bandId;
              EventsService.addEvent(bandId, gig).then(function(data) {
                $scope.message = data.data.message;
              }, function(err) {
              //  console.log(err);
              });
              $scope.booked = true;
           });

          };

          //paywall
          $scope.paywall = 1;//hide
          $scope.setPaywall = function(item){
            $scope.paywall = item;
              if (item === 0) {
                $('.calB').prop('disabled', true);
                  $scope.paywall = item;
                  $('.paywall').siblings().addClass('blur');
                  $('.paywall').addClass('show slide');

              } else if (item === 1) {
                $('.calB').prop('disabled', false);
                $scope.paywall = 1;
                $('.paywall').siblings().removeClass('blur');
              }


          };
          $scope.isPaywall = function(item, day){
            if(item === $scope.paywall){
              return true;
            }else{
              return false;
            }
          };

        },
        link: function(scope,el,attr) {

        }
      };
    });
}());
