/*
 * Copyright 2017, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.persistence.ui

import android.databinding.DataBindingUtil
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.android.persistence.R
import com.example.android.persistence.databinding.CommentItemBinding
import com.example.android.persistence.model.Comment

class CommentAdapter(private val mCommentClickCallback: CommentClickCallback?) : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    private lateinit var mCommentList: List<Comment>

    fun setCommentList(comments: List<Comment>) {
        if (mCommentList.isEmpty()) {
            mCommentList = comments
            notifyItemRangeInserted(0, comments.size)
        } else {
            val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun getOldListSize(): Int = mCommentList.size


                override fun getNewListSize(): Int  = comments.size

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    val old = mCommentList[oldItemPosition]
                    val comment = comments[newItemPosition]
                    return old.id == comment.id
                }

                override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    val old = mCommentList[oldItemPosition]
                    val comment = comments[newItemPosition]
                    return old.id == comment.id
                            && old.postedAt === comment.postedAt
                            && old.productId == comment.productId
                            && old.text == comment.text
                }
            })
            mCommentList = comments
            diffResult.dispatchUpdatesTo(this)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val binding = DataBindingUtil
                .inflate<CommentItemBinding>(LayoutInflater.from(parent.context), R.layout.comment_item,
                        parent, false)
        binding.callback = mCommentClickCallback
        return CommentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.binding.comment = mCommentList[position]
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int = mCommentList.size

    class CommentViewHolder(val binding: CommentItemBinding) : RecyclerView.ViewHolder(binding.root)
}
