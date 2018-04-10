import {Component, OnInit} from '@angular/core';
import {Principal, ResponseWrapper} from '../shared';
import {Tag, TagService} from '../entities/tag';

@Component( {
                selector: 'jhi-tag-overview', templateUrl: './tag-overview.component.html', styleUrls: ['overview.css']
            } )
export class TagOverviewComponent implements OnInit {

    tags: Tag[] = [];

    constructor( private principal: Principal, private tagService: TagService ) {
    }

    ngOnInit() {
        this.loadTags();
    }

    loadTags() {
        this.tagService.query()
            .subscribe( ( data: ResponseWrapper ) => this.tags = data.json );
    }

}
