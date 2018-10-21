import { Route } from '@angular/router';
import { OverviewComponent } from './overview.component';
import { ProductDetailComponent } from './product-detail.component';
import { ProductOrderComponent } from './order/product-order.component';
import { TagOverviewComponent } from './tag-overview.component';
import { ProductResolve } from 'app/entities/product';

export const SHOP_ROUTE: Route[] = [
    {
        path: 'shop',
        component: OverviewComponent,
        data: {
            authorities: [],
            pageTitle: 'home.title'
        }
    },
    {
        path: 'shop/tags',
        component: TagOverviewComponent,
        data: {
            authorities: [],
            pageTitle: 'home.title'
        }
    },
    {
        path: 'shop/product/:id',
        component: ProductDetailComponent,
        data: {
            authorities: [],
            pageTitle: 'home.title'
        }
    },
    {
        path: 'shop/product/:id/order',
        component: ProductOrderComponent,
        resolve: {
            product: ProductResolve
        },
        data: {
            authorities: ['USER_USER'],
            pageTitle: 'home.title'
        }
    }
];
