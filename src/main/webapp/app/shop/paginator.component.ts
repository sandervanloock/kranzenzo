import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Product } from 'app/shared/model/product.model';
import { Page } from 'app/entities/product';
import { PageEvent } from '@angular/material';

@Component({
    selector: 'jhi-paginator',
    templateUrl: './paginator.component.html',
    styles: []
})
export class PaginatorComponent implements OnInit {
    @Input() page: Page<Product> = new Page<Product>(0, 0, []);
    @Output() updatePage: EventEmitter<PageEvent> = new EventEmitter<PageEvent>();

    constructor() {}

    ngOnInit() {}

    updatePageHandler(event: PageEvent) {
        this.updatePage.emit(event);
    }
}
