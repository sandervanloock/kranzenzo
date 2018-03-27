import {Route} from '@angular/router';
import {OverviewComponent} from './overview.component';
import {ProductDetailComponent} from './product-detail.component';
import {ProductOrderPopupComponent} from './order/product-order.component';

export const SHOP_ROUTE: Route[] = [{
    path: 'shop', component: OverviewComponent, data: {
        authorities: [], pageTitle: 'home.title'
    }
}, {
    path: 'shop/product/:id', component: ProductDetailComponent, data: {
        authorities: [], pageTitle: 'home.title'
    }
}, {
    path: 'shop/product/:id/order', component: ProductOrderPopupComponent, data: {
        authorities: ['USER_USER'], pageTitle: 'home.title'
    }, outlet: 'popup'
}];
