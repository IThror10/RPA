module.exports = {
  devServer: {
    port: 8000,
    proxy: {
      '/': {
        target: 'http://localhost:8080',
        ws: false,
        changeOrigin: true
      }
    }
  }
}