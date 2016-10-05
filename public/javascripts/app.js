
// Main app module
var app = angular.module('myApp', ['ui.bootstrap', 'confirmDialogBoxModule']);

app.controller('bookCtrl', function($scope, $http, $timeout, $uibModal, BookService) {

 $scope.books = [];

    function getAllBook() {
       BookService.getAll().then(function(res){
             $scope.books = res.data;
        }, function(err){
           // error
        });
     }

      $scope.selectedBook = {};

      $scope.editBook = function(book){
            $scope.selectedBook = angular.copy(book);
      };

      $scope.updateBook = function(){
         BookService.updateBook($scope.selectedBook).then(function(res) {
             $('.modal').modal('hide');
             showAlertMessage(res.status, res.msg);
             getAllBook();
         }, function(err){
             // error
        });
      }

    $scope.newBook = {};

    $scope.addBook = function() {
        BookService.addBook($scope.newBook).then(function(res) {
                  $('.modal').modal('hide');
                  var newId = res.data.id;
                  $scope.newBook["id"] = newId;
                  $scope.books.push($scope.newBook);
                  $scope.newBook ={};
                  showAlertMessage(res.status, res.msg);
          }, function(err){
                // error
          });
    }

     $scope.deleteBook = function(bookId) {
           BookService.deleteBook(bookId).then(function(res){
                       var newBookList=[];
                       angular.forEach($scope.books,function(book){
                                if(book.id != bookId) {
                                        newBookList.push(book);
                                 }
                        });
                        $scope.books = newBookList;
             showAlertMessage(res.status, res.msg);
         }, function(err){
                 // error
          });
      }

    getAllBook();

    $scope.alerts = [];

    function showAlertMessage(status, message) {
              if(status == "success") {
                    $scope.alerts.push({type: "alert-success", title: "SUCCESS", content: message});
              } else if(status == "error") {
                     $scope.alerts.push({type: "alert-danger", title: "ERROR", content: message});
              }
    };

  });


/**
 * Directive for alert notification. You can also use angular ui-bootstrap for better alert notifications
 */
app.directive('notification', function($timeout){
  return {
    restrict: 'E',
    replace: true,
    scope: {
      ngModel: '='
    },
    template: '<div ng-class="ngModel.type" class="alert alert-box">{{ngModel.content}}</div>',
     link: function(scope, element, attrs) {
          $timeout(function(){
            element.hide();
          }, 3000);
      }
  }
});



/**
 * BookService: Provides all book services and run asynchronously
 */
app.service("BookService", function($http, $q) {

   var task = this;
   task.taskList = {};

   task.getAll = function() {
          var defer = $q.defer();
          $http.get('/book/list')
          .success(function(res){
                task.taskList = res;
                defer.resolve(res);
           })
           .error(function(err, status){
              defer.reject(err);
           });

         return defer.promise;
     }

   task.deleteBook = function(id) {
        var defer = $q.defer();
        $http.get('/book/delete?bookId=' + id)
        .success(function(res){
               task.taskList = res;
                defer.resolve(res);
         }).error(function(err, status){
               defer.reject(err);
         });

         return defer.promise
   }

   task.updateBook = function(data) {
      var defer = $q.defer();
      $http.post('/book/update', data)
      .success(function(res){
               task.taskList = res;
               defer.resolve(res);
       }).error(function(err, status){
                defer.reject(err);
       });

       return defer.promise
   }

   task.addBook = function(data) {
         var defer = $q.defer();
         $http.post('/book/create', data)
         .success(function(res){
                task.taskList = res;
                defer.resolve(res);
         })
         .error(function(err, status){
                defer.reject(err);
         });;

          return defer.promise
      }

   return task;

 });


/**
 * Module for confirm dialog box
 * To use this, add this module as a dependency in app module.
 */
angular.module('confirmDialogBoxModule', ['ui.bootstrap'])
  .directive('ngConfirmClick', ['$uibModal', function($uibModal) {

      var modalInstanceCtrl = function($scope, $uibModalInstance) {
        $scope.ok = function() {
          $uibModalInstance.close();
        };

        $scope.cancel = function() {
          $uibModalInstance.dismiss('cancel');
        };
      };

      return {
        restrict: 'A',
        scope:{
          ngConfirmClick:"&"
        },
        link: function(scope, element, attrs) {
          element.bind('click', function() {
            var message = attrs.ngConfirmMessage || "Are you sure ?";

            // Template for confirmation dialog box
            var modalHtml = '<div class="modal-body">' + message + '</div>';
            modalHtml += '<div class="modal-footer"><button class="btn btn-primary" ng-click="ok()">OK</button><button class="btn btn-default" ng-click="cancel()">Cancel</button></div>';

            var modalInstance = $uibModal.open({
              template: modalHtml,
              controller: modalInstanceCtrl
            });

            modalInstance.result.then(function() {
              scope.ngConfirmClick();
            }, function() {
              //Modal dismissed
            });
          });

        }
      }
    }
  ]);
