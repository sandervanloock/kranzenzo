import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Principal } from 'app/core';
import { ITag } from 'app/shared/model/tag.model';
import { TagService } from 'app/entities/tag';
import { HttpResponse } from '@angular/common/http';

@Component({
    selector: 'jhi-tag-overview',
    templateUrl: './tag-overview.component.html',
    styleUrls: ['overview.css', 'tag-overview.css']
})
export class TagOverviewComponent implements OnInit {
    tags: ITag[] = [];
    parentId: number;

    constructor(private principal: Principal, private tagService: TagService, private route: ActivatedRoute) {}

    ngOnInit() {
        this.route.queryParams.subscribe(params => {
            if (params.parentId) {
                this.parentId = params.parentId;
            }
            this.loadTags();
        });
    }

    loadTags() {
        this.tagService.query({ parentId: this.parentId }).subscribe((data: HttpResponse<ITag[]>) => (this.tags = data.body));
    }
}
