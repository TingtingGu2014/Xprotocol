import * as utils from '../utils/Utils.js'
import * as userUtils from '../utils/UserUtils.js'

export default ({ Vue }) => {
  Vue.prototype.$utils = utils
  Vue.prototype.$userUtils = userUtils
}