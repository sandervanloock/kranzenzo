import {Component, OnInit} from '@angular/core';
import {Principal, ResponseWrapper} from '../shared';
import {Tag, TagService} from '../entities/tag';
import {ActivatedRoute} from '@angular/router';

@Component( {
                selector: 'jhi-tag-overview', templateUrl: './tag-overview.component.html', styleUrls: ['overview.css']
            } )
export class TagOverviewComponent implements OnInit {

    tags: Tag[] = [];
    parentId: number;

    constructor( private principal: Principal, private tagService: TagService, private route: ActivatedRoute ) {
    }

    ngOnInit() {
        this.route.queryParams
            .subscribe( ( params ) => {
                if ( params.parentId ) {
                    this.parentId = params.parentId;
                }
                this.loadTags();
            } );
    }

    loadTags() {
        this.tagService.query( {parentId: this.parentId} )
            .subscribe( ( data: ResponseWrapper ) => this.tags = data.json );
    }

}
