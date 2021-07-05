import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { Location } from '@angular/common';
import { ActivatedRoute, Params } from '@angular/router';
import { Principal } from 'app/core';
import { Page, ProductService } from 'app/entities/product';
import { IProduct, Product } from 'app/shared/model/product.model';
import { HttpResponse } from '@angular/common/http';
import { TagService } from 'app/entities/tag';
import { SearchState } from 'app/entities/product/product.service';
import { PageEvent } from '@angular/material';

@Component({
    selector: 'jhi-overview',
    templateUrl: './overview.component.html',
    styleUrls: ['overview.css'],
    encapsulation: ViewEncapsulation.None
})
export class OverviewComponent implements OnInit {
    items: Product[] = [];
    page: Page<Product> = new Page<Product>(0, 0, []);
    searchState: SearchState;
    showSpinner = true;
    breakpoint: number;

    constructor(
        private principal: Principal,
        private productService: ProductService,
        private route: ActivatedRoute,
        private tagService: TagService,
        private location: Location
    ) {}

    ngOnInit() {
        const queryParams: Params = Object.assign({}, this.route.snapshot.queryParams);
        this.searchState = new SearchState(
            parseInt(queryParams['page'], 10),
            queryParams['name'],
            queryParams['nameAsInteger'],
            parseInt(queryParams['tags'], 10),
            true,
            queryParams['sort']
        );
        this.reloadState();
        this.breakpoint = this.getNumberOfBreakpointsFromWidth(window.innerWidth);
    }

    onResize(event) {
        this.breakpoint = this.getNumberOfBreakpointsFromWidth(event.target.innerWidth);
    }

    private getNumberOfBreakpointsFromWidth(width) {
        return width <= 650 ? 1 : width <= 800 ? 2 : width <= 995 ? 3 : 4;
    }

    updateSearch() {
        this.searchState.page = 0;
        this.reloadState();
    }

    reloadState() {
        this.showSpinner = true;
        this.productService.search(this.searchState).subscribe((data: HttpResponse<Page<IProduct>>) => {
            this.page = data.body;
            this.items = data.body.content;
            this.showSpinner = false;
        });
        this.location.go('shop', this.searchState.toQuery());
    }

    updatePage(event: PageEvent) {
        window.scroll(0, 0);
        this.searchState.page = event.pageIndex;
        this.reloadState();
    }
}
