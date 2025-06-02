// 登录
export function loginApi(data) {
    return request({
        url: '/user/loginByEmail',
        method: 'post',
        data
    })
}

function loginoutApi() {
  return $axios({
    'url': '/user/loginout',
    'method': 'post',
  })
}

  