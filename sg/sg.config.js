const open_env_type = "uat";//部署环境 dev: 开发环境，test: 部署环境， prod: 生产环境
const open_isc_login_type = "new"; //isc 登录方式

const open_locationUrl = location.origin + location.pathname;
const open_client_id= "867aa9701854426c9db7fc9e68e84b3c";
if(open_env_type === "dev"){
  window.configInfo = {
    fileUrlAddress: "http://127.0.0.1:4321", //文件服务地址
    apiDocumentUrl: "https://wechtpoc.ehome.ren:10443/api/doc#11378", //工作台展示自定义内容API文档
    iscLoginUrl: open_isc_login_type === "old" ? `http://iscsso.sgcc.com.cn:22002/isc_sso/login?service=${open_locationUrl}`: `http://iscmp.sgcc.com.cn:3001/sgid-sso/oauth2.0/authorize?response_type=token&client_id=${open_client_id}&redirect_uri=${open_locationUrl}`,
    newsUrlAddress: "http://127.0.0.1:8080", //图文推送文章地址
    publicKey: "044dca2a0dfb22bd3a52a603d7c013d79ff474abc522cfa56c0a0c873257b338a88699f7aacd0179cd8a6e5300814e0c7d24b04bbb3ad6114adbc78630860f8b92", //登录加密公钥
    iscLogOutUrl(userId){
      return `http://iscmp.sgcc.com.cn:3001/sgid-sso/logout?iscUserId=${userId}&redirect_uri=${open_locationUrl}`
    },
    homeLogo: 'logo.jpg',
    adminLogo: 'logo.png',
  }
}else if(open_env_type === "test"){
  window.configInfo = {
    fileUrlAddress: "http://192.168.13.7/file", //文件服务地址
    apiDocumentUrl: "https://wechtpoc.ehome.ren:10443/api/doc#11378", //工作台展示自定义内容API文档
    iscLoginUrl: open_isc_login_type === "old" ? `http://iscsso.sgcc.com.cn:22002/isc_sso/login?service=${open_locationUrl}`: `http://iscmp.sgcc.com.cn:3001/sgid-sso/oauth2.0/authorize?response_type=token&client_id=${open_client_id}&redirect_uri=${open_locationUrl}`,
    newsUrlAddress: "http://192.168.13.7", //图文推送文章地址
    publicKey: "044dca2a0dfb22bd3a52a603d7c013d79ff474abc522cfa56c0a0c873257b338a88699f7aacd0179cd8a6e5300814e0c7d24b04bbb3ad6114adbc78630860f8b92",
    iscLogOutUrl(userId){
      return `http://iscmp.sgcc.com.cn:3001/sgid-sso/logout?iscUserId=${userId}&redirect_uri=${open_locationUrl}`
    },
    homeLogo: 'logo.jpg',
    adminLogo: 'logo.png',
  }

}else if(open_env_type === "prod"){
  window.configInfo = {
    fileUrlAddress: "http://127.0.0.1:8085/file", //文件服务地址
    apiDocumentUrl: "https://wechtpoc.ehome.ren:10443/api/doc#11378", //工作台展示自定义内容API文档
    iscLoginUrl: open_isc_login_type === "old" ? `http://iscsso.sgcc.com.cn:22002/isc_sso/login?service=${open_locationUrl}`: `http://iscmp.sgcc.com.cn:3001/sgid-sso/oauth2.0/authorize?response_type=token&client_id=${open_client_id}&redirect_uri=${open_locationUrl}`,
    newsUrlAddress: "http://127.0.0.1:8080", //图文推送文章地址
    publicKey: "044dca2a0dfb22bd3a52a603d7c013d79ff474abc522cfa56c0a0c873257b338a88699f7aacd0179cd8a6e5300814e0c7d24b04bbb3ad6114adbc78630860f8b92",
    iscLogOutUrl(userId){
      return `http://iscmp.sgcc.com.cn:3001/sgid-sso/logout?iscUserId=${userId}&redirect_uri=${open_locationUrl}`
    },
    homeLogo: 'logo.jpg',
    adminLogo: 'logo.png',
  }
}else if(open_env_type === "pms"){
  window.configInfo = {
    fileUrlAddress: "http://192.168.13.22/file", //文件服务地址
    apiDocumentUrl: "https://wechtpoc.ehome.ren:10443/api/doc#11378", //工作台展示自定义内容API文档
    iscLoginUrl: open_isc_login_type === "old" ? `http://iscsso.sgcc.com.cn:22002/isc_sso/login?service=${open_locationUrl}`: `http://iscmp.sgcc.com.cn:3001/sgid-sso/oauth2.0/authorize?response_type=token&client_id=${open_client_id}&redirect_uri=${open_locationUrl}`,
    newsUrlAddress: "http://192.168.13.22", //图文推送文章地址
    publicKey: "044dca2a0dfb22bd3a52a603d7c013d79ff474abc522cfa56c0a0c873257b338a88699f7aacd0179cd8a6e5300814e0c7d24b04bbb3ad6114adbc78630860f8b92",
    iscLogOutUrl(userId){
      return `http://iscmp.sgcc.com.cn:3001/sgid-sso/logout?iscUserId=${userId}&redirect_uri=${open_locationUrl}`
    },
    homeLogo: 'logo01.png',
    adminLogo: 'logo02.png',
  }
}else if(open_env_type === "innerTest"){
  window.configInfo = {
    fileUrlAddress: "http://192.168.13.24/file", //文件服务地址
    apiDocumentUrl: "https://wechtpoc.ehome.ren:10443/api/doc#11378", //工作台展示自定义内容API文档
    iscLoginUrl: open_isc_login_type === "old" ? `http://iscsso.sgcc.com.cn:22002/isc_sso/login?service=${open_locationUrl}`: `http://iscmp.sgcc.com.cn:3001/sgid-sso/oauth2.0/authorize?response_type=token&client_id=${open_client_id}&redirect_uri=${open_locationUrl}`,
    newsUrlAddress: "http://192.168.13.24", //图文推送文章地址
    publicKey: "044dca2a0dfb22bd3a52a603d7c013d79ff474abc522cfa56c0a0c873257b338a88699f7aacd0179cd8a6e5300814e0c7d24b04bbb3ad6114adbc78630860f8b92",
    iscLogOutUrl(userId){
      return `http://iscmp.sgcc.com.cn:3001/sgid-sso/logout?iscUserId=${userId}&redirect_uri=${open_locationUrl}`
    },
    homeLogo: 'logo.jpg',
    adminLogo: 'logo.png',
  }
}else if(open_env_type === "uat"){
  window.configInfo = {
    fileUrlAddress: "http://igw.isgcc.net:18203/file", //文件服务地址
    apiDocumentUrl: "https://wechtpoc.ehome.ren:10443/api/doc#11378", //工作台展示自定义内容API文档
    iscLoginUrl: open_isc_login_type === "old" ? `https://igw.isgcc.net:18443/isc_sso/login?service=${open_locationUrl}`: `https://igw.isgcc.net:18443/sgid-sso/oauth2.0/authorize?response_type=token&client_id=${open_client_id}&redirect_uri=${open_locationUrl}`,
    newsUrlAddress: "http://igw.isgcc.net:18203", //图文推送文章地址
    publicKey: "044dca2a0dfb22bd3a52a603d7c013d79ff474abc522cfa56c0a0c873257b338a88699f7aacd0179cd8a6e5300814e0c7d24b04bbb3ad6114adbc78630860f8b92",
    iscLogOutUrl(userId){
      return `https://igw.isgcc.net:18443/sgid-sso/logout?iscUserId=${userId}&redirect_uri=${open_locationUrl}`
    },
    homeLogo: 'logo.jpg',
    adminLogo: 'logo.png',
  }
}

