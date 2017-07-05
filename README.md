# RecyclerBanner

Use RecyclerView to create Banner


RecyclerBanner For reference only;


master Based on `RelativeLayout`

This branch Based on `FrameLayout`


> Screenshots

[gif](https://github.com/7449/BannerLayout/blob/RecyclerBanner/app/RecyclerBanner.gif)


![](https://github.com/7449/BannerLayout/blob/RecyclerBanner/app/RecyclerBanner.gif)

> Simple to use


Bean class please implement `BannerModelCallBack`

Specific reference `SimpleBannerModel`

    RecyclerBannerLayout
            .initListResources(initImageModel())//initData
            .initTips(true, true, true)//settings tips
            .start(true, 2000)

If you use the built-in frame, please rely on Glide

Because in the frame

	provided 'com.github.bumptech.glide:glide:3.7.0'


> Click the event


        recyclerBannerLayout
                .setOnRecyclerBannerClickListener(new OnRecyclerBannerClickListener<SimpleRecyclerBannerModel>() {
                    @Override
                    public void onBannerClick(View view, int position, SimpleRecyclerBannerModel model) {
                        Toast.makeText(view.getContext(), "banner click", Toast.LENGTH_SHORT).show();
                    }
                });


> Use the Custom Load Picture frame

	public class ImageManager implements RecyclerBannerImageLoaderManager<SimpleRecyclerBannerModel> {
	
	    @Override
	    public void display(ImageView imageView, SimpleRecyclerBannerModel model) {
	        Picasso.with(imageView.getContext())
	                .load((String) model.getRecyclerBannerImageUrl())
	                .placeholder(R.mipmap.ic_launcher)
	                .error(R.mipmap.ic_launcher)
	                .into(imageView);
	    }
	}

	recyclerBannerLayout
	                .setRecyclerImageLoaderManager(new ImageManager())

License
--
	Copyright (C) 2016 yuebigmeow@gamil.com

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
