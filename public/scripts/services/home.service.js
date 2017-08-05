/**
 * Created by Xinlin on 2015/12/4.
 */
(function () {
    'use strict';

    app.value('valueList',{})
        .factory('menu', [
            '$location',
            '$rootScope',
            '$http',
            '$timeout',
            function ($location, $rootScope, $http, $timeout) {

                var sections = [{
                    "name": "数据链条化",
                    "type": "toggle",
                    "pages": [
                        {
                            "name": "表关系网图",
                            "url": "dataView",
                            "type": "link"
                        },{
                            "name": "表关系网列表",
                            "url": "tableDataList",
                            "type": "link"
                        }]
                }];
                // $http.get('json_cache/menuJson.json').then(function (responseData) {
                //     for(var i in responseData){
                //         sections.push(responseData[i]);
                //     }
                // });
                var self;

                self = {
                    sections: sections,

                    selectSection: function (section) {
                        self.openedSection = section;
                    },
                    toggleSelectSection: function (section) {
                        self.openedSection = (self.openedSection === section ? null : section);
                    },
                    isSectionSelected: function (section) {
                        return self.openedSection === section;
                    },

                    selectPage: function (section, page) {
                        self.currentSection = section;
                        self.currentPage = page;
                    },
                    isPageSelected: function (page) {
                        return self.currentPage === page;
                    }
                };

                $rootScope.$on('$locationChangeSuccess', onLocationChange);

                function onLocationChange() {
                    var path = $location.path();
                    var introLink = {
                        name: "华泰然车辆管理",
                        url: "/",
                        type: "link"
                    };

                    if (path == '/') {
                        self.selectSection(introLink);
                        self.selectPage(introLink, introLink);
                        return;
                    }

                    var matchPage = function (section, page) {
                        if (path.indexOf(page.url) !== -1) {
                            self.selectSection(section);
                            self.selectPage(section, page);
                        }
                    };

                    sections.forEach(function (section) {
                        if (section.children) {
                            // matches nested section toggles, such as API or Customization
                            section.children.forEach(function (childSection) {
                                if (childSection.pages) {
                                    childSection.pages.forEach(function (page) {
                                        matchPage(childSection, page);
                                    });
                                }
                            });
                        }
                        else if (section.pages) {
                            // matches top-level section toggles, such as Demos
                            section.pages.forEach(function (page) {
                                matchPage(section, page);
                            });
                        }
                        else if (section.type === 'link') {
                            // matches top-level links, such as "Getting Started"
                            matchPage(section, section);
                        }
                    });
                }

                return self;
            }]);
})();
