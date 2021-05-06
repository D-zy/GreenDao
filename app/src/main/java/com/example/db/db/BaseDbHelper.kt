package com.example.db.db

import org.greenrobot.greendao.AbstractDao
import org.greenrobot.greendao.query.QueryBuilder

/**
 * 文 件 名: BaseDbHelper
 * 说   明:  greedDAO 基础辅助类
 */
open class BaseDbHelper<T, K>(dao: AbstractDao<T, K>) {
    private val mDao: AbstractDao<T, K> = dao

    fun save(item: T) {
        mDao.insert(item)
    }

    fun save(vararg items: T) {
        mDao.insertInTx(*items)
    }

    fun save(items: MutableList<T>) {
        mDao.insertInTx(items)
    }

    fun saveOrUpdate(item: T) {
        mDao.insertOrReplace(item)
    }

    fun saveOrUpdate(vararg items: T) {
        mDao.insertOrReplaceInTx(*items)
    }

    fun saveOrUpdate(items: MutableList<T>) {
        mDao.insertOrReplaceInTx(items)
    }

    fun deleteByKey(key: K) {
        mDao.deleteByKey(key)
    }

    fun delete(item: T) {
        mDao.delete(item)
    }

    fun delete(vararg items: T) {
        mDao.deleteInTx(*items)
    }

    fun delete(items: MutableList<T>) {
        mDao.deleteInTx(items)
    }

    fun deleteAll() {
        mDao.deleteAll()
    }

    fun update(item: T) {
        mDao.update(item)
    }

    fun update(vararg items: T) {
        mDao.updateInTx(*items)
    }

    fun update(items: MutableList<T>) {
        mDao.updateInTx(items)
    }

    fun query(key: K): T {
        return mDao.load(key)
    }

    fun queryAll(): MutableList<T> {
        return mDao.loadAll()
    }

    fun query(where: String?, vararg params: String?): MutableList<T> {
        return mDao.queryRaw(where, *params)
    }

    fun queryBuilder(): QueryBuilder<T> {
        return mDao.queryBuilder()
    }

    fun count(): Long {
        return mDao.count()
    }

    fun refresh(item: T) {
        mDao.refresh(item)
    }

    fun detach(item: T) {
        mDao.detach(item)
    }

}