###
  Service
###
class BookService

  constructor: (@$http, @$q) ->
    @taskList = {}

  getAll: ->
    defer = @$q.defer()
    @$http.get('/book/list')
    .success (res) =>
      @taskList = res
      defer.resolve(res)
    .error (err, status) => defer.reject err

    defer.promise


  deleteBook: (title) ->
    defer = @$q.defer()
    @$http.get "/book/delete?title=#{ title }"
    .success (res) =>
      @taskList = res
      defer.resolve res
    .error (err, status) => defer.reject err

    defer.promise

  updateBook: (data) ->
    defer = @$q.defer()
    @$http.post '/book/update', data
    .success (res) =>
      @taskList = res
      defer.resolve res
    .error (err, status) => defer.reject err

    defer.promise

  addBook: (data) ->
    defer = @$q.defer()
    @$http.post '/book/create', data
    .success (res) =>
      @taskList = res
      defer.resolve res
    .error (err, status) => defer.reject err

    defer.promise


###
  Controller
###
class BookCtrl

  constructor: (@$scope, @$http, @$timeout, @$uibModal, @$window, @BookService) ->
    @books = []
    @alerts = []
    @selectedBook = {}
    @newBook = {}
    @getAllBooks()

  showAlertMessage: (status, message) ->
    switch status
      when "success" then @alerts.push(
        type: "alert-success"
        title: "SUCCESS"
        content: message
      )
      when "error" then @alerts.push(
        type: "alert-danger"
        title: "ERROR"
        content: message
      )

  getAllBooks: ->
    @BookService.getAll().then(
      (res) => @books = res.data
    , (err) => @showAlertMessage "error", err
    )

  editBook: (book) ->
    @selectedBook = angular.copy(book)

  updateBook: ->
    @BookService.updateBook(@selectedBook).then(
      (res) =>
        $('.modal').modal 'hide'
        @getAllBooks()
        @showAlertMessage res.status, res.msg
    , (err) => @showAlertMessage "error", err
    )

  addBook: ->
    @BookService.addBook(@newBook).then(
      (res) =>
        $('.modal').modal 'hide'
        @getAllBooks()
        @showAlertMessage res.status, res.msg
    , (err) => @showAlertMessage "error", err
    )

  deleteBook: (title) ->
    @BookService.deleteBook(title).then(
      (res) =>
        newBookList=[]
        angular.forEach @books, (book) =>
          newBookList.push(book) if book.title != title
        @books = newBookList
        @showAlertMessage res.status, res.msg
    , (err) => @showAlertMessage "error", err
    )

  infoSearch: (book) ->
    @$http.post '/book/infoSearch', book
    .success (res) => @$window.open res.data, '_blank'
    .error (err, status) =>

###
  Module (main)
###
app = angular.module 'myApp', ['ui.bootstrap', 'confirmDialogBoxModule']
             .service 'BookService', ['$http', '$q', BookService]
             .controller 'BookCtrl', ['$scope', '$http', '$timeout', '$uibModal', '$window', 'BookService', BookCtrl]

###
  Directive
###
app.directive 'notification', ($timeout) ->
  restrict: 'E'
  replace: true
  scope: {ngModel: '='}
  template: '<div ng-class="ngModel.type" class="alert alert-box">{{ ngModel.content }}</div>'
  link: (scope, element, attrs) ->
    $timeout(
      () -> element.hide(),
      3000
    )

###
  Module (confirm dialog box)
###
angular
.module('confirmDialogBoxModule', ['ui.bootstrap'])
.directive('ngConfirmClick', [
    '$uibModal',
    ($uibModal) ->
      modalInstanceCtrl = ($scope, $uibModalInstance) ->
        $scope.ok = ->
          $uibModalInstance.close()
        $scope.cancel = ->
          $uibModalInstance.dismiss('cancel')

      restrict: 'A',
      scope: {ngConfirmClick: "&"},
      link: (scope, element, attrs) ->
        element.bind 'click', ->
          message = attrs.ngConfirmMessage || "Are you sure ?"

          # Template for confirmation dialog box
          modalHtml ='<div class="modal-body">' + message + '</div>'
          modalHtml += """
                       <div class="modal-footer">
                         <button class="btn btn-primary" ng-click="ok()">OK</button>
                       <button class="btn btn-default" ng-click="cancel()">Cancel</button>
                     </div>
                     """

          modalInstance = $uibModal.open
            template: modalHtml,
            controller: modalInstanceCtrl

          modalInstance.result.then(->
            scope.ngConfirmClick()
          , -> #Modal dismissed
          )
  ]
)
