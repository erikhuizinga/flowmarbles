config.devServer = config.devServer || {};
const existingProxy = Array.isArray(config.devServer.proxy) ? config.devServer.proxy : [];
config.devServer.proxy = existingProxy.concat([
  {
    context: ["/docs"],
    target: "http://localhost:8000",
    changeOrigin: true,
    pathRewrite: { "^/docs": "" }
  }
]);
