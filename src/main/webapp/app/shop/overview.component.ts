import { Component, OnInit } from '@angular/core';
import { Location } from '@angular/common';
import { ActivatedRoute, Params } from '@angular/router';
import { Principal } from 'app/core';
import { Page, ProductService } from 'app/entities/product';
import { IProduct, Product } from 'app/shared/model/product.model';
import { HttpResponse } from '@angular/common/http';
import { ITag } from 'app/shared/model/tag.model';
import { TagService } from 'app/entities/tag';
import { SearchState } from 'app/entities/product/product.service';
import { PageEvent } from '@angular/material';

@Component({
    selector: 'jhi-overview',
    templateUrl: './overview.component.html',
    styleUrls: ['overview.css']
})
export class OverviewComponent implements OnInit {
    items: Product[] = [];
    page: Page<Product> = new Page<Product>(0, 0, []);
    searchState: SearchState;

    tags: ITag[];

    constructor(
        private principal: Principal,
        private productService: ProductService,
        private route: ActivatedRoute,
        private tagService: TagService,
        private location: Location
    ) {}

    ngOnInit() {
        const queryParams: Params = Object.assign({}, this.route.snapshot.queryParams);
        this.searchState = new SearchState(queryParams['query'], parseInt(queryParams['tag'], 10), true);
        this.reloadState();
        this.tagService.query().subscribe((res: HttpResponse<ITag[]>) => {
            this.tags = res.body;
        });
    }

    updateSearch() {
        this.searchState.page = 0;
        this.reloadState();
    }

    reloadState() {
        this.productService.search(this.searchState).subscribe((data: HttpResponse<Page<IProduct>>) => {
            this.page = data.body;
            this.items = data.body.content;
        });
        this.location.go('shop', this.searchState.toQuery());
    }

    updatePage(event: PageEvent) {
        this.searchState.page = event.pageIndex;
        this.reloadState();
    }
}
