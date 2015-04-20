(function () {
    'use strict';



var controllers = angular.module('controllers', []);

controllers.controller('ContactsCtrl', ['$scope', '$http', function($scope, $http) {

    $http.get('../contacts')
        .success(function (response) {
            $scope.contacts = response;
        })
        .error(function (response) {
            console.log(response)
        });

}]);

controllers.controller('MainCtrl', ['$scope', function($scope) {
    $scope.testMessage = "Check out our home!";
}]);

controllers.controller('CreateContactCtrl', ['$scope', 'ContactService', '$location','$http', function($scope, ContactService, $location, $http) {

    $scope.errorMessage = "";
    $scope.success = null;


    $scope.submit = function() {

        $http.post("../contacts", $scope.contact)
            .success(function (response) {
                console.log(response);
                $scope.success = true;
                $scope.newContactForm.$setPristine();
                $scope.contact = "";
                $location.path($location.path());

            }).error(function(response) {
                console.log(response);
                $scope.errorMessage = response;
                $scope.success = false;
                $location.path($location.path());
            });
    };

}]);

controllers.controller('DetailsCtrl', ['$scope', '$http','$routeParams', 'ContactService', function($scope, $http, $routeParams, ContactService) {
    $scope.edit = false;
    $scope.success = null;
    $scope.errorMessage = "";
    $scope.addingEncounter = false;

    $http.get('../contacts/contact/' + $routeParams.id)
        .success(function (response) {
            $scope.contact = response;
        })
        .error(function (response) {
            //TODO
        });

    $scope.assessmentRange = [0,1,2,3,4,5,6,7,8,9,10];

    $scope.addEncounter = function() {

        //TODO: use a selected initiator
        $scope.newEncounter.initiator = null;
        $scope.newEncounter.form = null;

        if (null === $scope.contact.encounters) {
            $scope.contact.encounters = $scope.newEncounter;
        } else {
            var encounterList = $scope.contact.encounters;
            encounterList.concat($scope.newEncounter);
            $scope.contact.encounters = encounterList;
        }

        ContactService.update({id: $scope.contact.id}, $scope.contact);

        //$http.put("../contacts/contact/" + $scope.contact.contact_id, $scope.contact)
        //    .success(function (response) {
        //        console.log(response);
        //        $scope.success = true;
        //        $scope.newEncounterForm.$setPristine();
        //
        //    }).error(function(response) {
        //        console.log(response);
        //        $scope.errorMessage = response;
        //        $scope.success = false;
        //    });

    };

    $scope.submit = function() {
        $http.put("../contacts/contact/" + $scope.contact.id, $scope.contact)
            .success(function (response) {
                console.log(response);
                $scope.success = true;

            }).error(function(response) {
                console.log(response);
                $scope.errorMessage = response;
                $scope.success = false;
            });
    };
}]);

}());

