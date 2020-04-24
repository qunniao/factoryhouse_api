var params = {
    periodTimePOList: {},
    userMangerss: {}
};


var api = {
    serverHost:"https://back.zhanchengwlkj.com/factoryhouse",
    //serverHost:"http://localhost",
    //serverHost:"https://back.zhanchengwlkj.com/atxca",
    adminLoginByPassWord:"/login/adminLoginByPassWord",
    findListByPhone:"",
    findAllModule:"/modules/findAllModule",
    pagePersonal:"/personal/pagePersonal",
    deleteUser:"/personal/deleteUser",

    pageAdministratorAccount:"/administrator/pageAdministratorAccount",
    updateAdministratorAccountByIsEnd:"/administrator/updateAdministratorAccountByIsEnd",
    updateAdministratorAccount:"/administrator/updateAdministratorAccount",
    addAdministratorAccount:"/administrator/addAdministratorAccount",
    deleteAdministratorAccount:"/administrator/deleteAdministratorAccount",

    pageProductList:"/productList/pageProductList",
    updateProductList:"/productList/updateProductList",
    addProductList:"/productList/addProductList",
    deleteProductList:"/productList/deleteProductList",

    uploadSystemPicture:"/picture/uploadSystemPicture",
    selectSystemPicture:"/picture/selectSystemPicture",
    delSystemPicture:"/picture/delSystemPicture",
    querySystemConfig:"/systemConfig/querySystemConfig",
    updateSystemConfig:"/systemConfig/updateSystemConfig",

    pageParkStore:"/parkStore/pageParkStore",
    updateParkStoreByStatus:"/parkStore/updateParkStoreByStatus",
    deleteParkStore:"/parkStore/deleteParkStore",

    pageNews:"/newsController/pageNews",
    updateNewsByIsEnd:"/newsController/updateNewsByIsEnd",
    deleteNews:"/newsController/deleteNews",
    addNews:"/newsController/addNews",
    uploadPicture:"/picture/uploadPicture",

    pageBuyingPurchase:"/buyingPurchase/pageBuyingPurchase",
    deleteBuyingPurchase:"/buyingPurchase/deleteBuyingPurchase",
    updateBuyingPurchaseByIsEnd:"/buyingPurchase/updateBuyingPurchaseByIsEnd",
    pageDelegationInformation:"/delegationInformation/pageDelegationInformation",
    deleteDelegationInformation:"/delegationInformation/deleteDelegationInformation",
    deleteEncyclopedia:"/encyclopedia/deleteEncyclopedia",
    deleteEncyclopediaAnswer:"/encyclopediaAnswer/deleteEncyclopediaAnswer",
    pageFplHouse:"/fplHouse/pageFplHouse",
    deleteFplHouse:"/fplHouse/deleteFplHouse",
    updateFplHouseByIsEnd:"/fplHouse/updateFplHouseByIsEnd",
};