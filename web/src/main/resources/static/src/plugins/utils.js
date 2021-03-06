import * as utils from '../utils/Utils.js'
import * as userUtils from '../utils/UserUtils.js'
import * as reqUtils from '../utils/ReqUtils.js'
import * as protocolUtils from '../utils/ProtocolUtils.js'

export default ({ Vue }) => {
  Vue.prototype.$utils = utils
  Vue.prototype.$userUtils = userUtils
  Vue.prototype.$reqUtils = reqUtils
  Vue.prototype.$protocolUtils = protocolUtils
}