// These constants are injected via webpack environment variables.
// You can add more variables in webpack.common.js or in profile specific webpack.<dev|prod>.js files.
// If you change the values in the webpack config files, you need to re run webpack to update the application

export const VERSION = process.env.VERSION;
export const DEBUG_INFO_ENABLED: boolean = !!process.env.DEBUG_INFO_ENABLED;
export const SERVER_API_URL = process.env.SERVER_API_URL;
export const BUILD_TIMESTAMP = process.env.BUILD_TIMESTAMP;
export const PRICE_BATTERIES_INCLUDED = 0.5;
export const PRICE_PER_KILOMETER_PER_KM = 0.5;
export const ORDER_DELIVERY_ORIGIN = { lat: 51.05541, lng: 4.490185 };
export const VAT_NUMBER = 'BE51 9733 4667 1162';
